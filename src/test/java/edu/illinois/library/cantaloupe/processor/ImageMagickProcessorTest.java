package edu.illinois.library.cantaloupe.processor;

import edu.illinois.library.cantaloupe.Application;
import edu.illinois.library.cantaloupe.image.ImageInfo;
import edu.illinois.library.cantaloupe.image.SourceFormat;
import edu.illinois.library.cantaloupe.request.OutputFormat;
import edu.illinois.library.cantaloupe.request.Quality;
import junit.framework.TestCase;
import org.apache.commons.configuration.BaseConfiguration;
import org.im4java.process.ProcessStarter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageMagickProcessorTest extends TestCase {

    ImageMagickProcessor instance;

    public void setUp() {
        BaseConfiguration config = new BaseConfiguration();
        config.setProperty("ImageMagickProcessor.path_to_binaries", "/usr/local/bin"); // TODO: externalize this
        Application.setConfiguration(config);

        instance = new ImageMagickProcessor();
    }

    public void testInitialization() {
        assertEquals(ProcessStarter.getGlobalSearchPath(), Application.
                getConfiguration().getString("ImageMagickProcessor.path_to_binaries"));
    }

    public void testGetImageInfo() throws Exception {
        // get an ImageInfo representing an image file
        File file = getFixture("escher_lego.jpg");
        InputStream is = new FileInputStream(file);
        SourceFormat sourceFormat = SourceFormat.JPG;
        String baseUri = "http://example.org/base/";
        ImageInfo info = instance.getImageInfo(is, sourceFormat, baseUri);

        assertEquals("http://iiif.io/api/image/2/context.json", info.getContext());
        assertEquals(baseUri, info.getId());
        assertEquals("http://iiif.io/api/image", info.getProtocol());
        assertEquals(594, (int) info.getWidth());
        assertEquals(522, (int) info.getHeight());

        List<Map<String, Integer>> sizes = info.getSizes();
        assertNull(sizes);

        List<Map<String,Object>> tiles = info.getTiles();
        assertNull(tiles);

        List<Object> profile = info.getProfile();
        assertEquals("http://iiif.io/api/image/2/level2.json", profile.get(0));

        List<String> actualFormats = (List<String>)((Map)profile.get(1)).get("formats");
        List<String> expectedFormats = new ArrayList<String>();
        for (OutputFormat outputFormat : OutputFormat.values()) {
            if (outputFormat != OutputFormat.WEBP) {
                expectedFormats.add(outputFormat.getExtension());
            }
        }
        assertEquals(expectedFormats, actualFormats);

        List<String> actualQualities = (List<String>)((Map)profile.get(1)).get("qualities");
        List<String> expectedQualities = new ArrayList<String>();
        for (Quality quality : Quality.values()) {
            expectedQualities.add(quality.toString().toLowerCase());
        }
        assertEquals(expectedQualities, actualQualities);

        List<String> actualSupports = (List<String>)((Map)profile.get(1)).get("supports");
        List<String> expectedSupports = new ArrayList<String>();
        expectedSupports.add("baseUriRedirect");
        expectedSupports.add("mirroring");
        expectedSupports.add("regionByPx");
        expectedSupports.add("rotationArbitrary");
        expectedSupports.add("rotationBy90s");
        expectedSupports.add("sizeByWhListed");
        expectedSupports.add("sizeByForcedWh");
        expectedSupports.add("sizeByH");
        expectedSupports.add("sizeByPct");
        expectedSupports.add("sizeByW");
        expectedSupports.add("sizeWh");
        assertEquals(expectedSupports, actualSupports);
    }

    public void testGetSupportedOutputFormats() {
        ArrayList<OutputFormat> expectedFormats = new ArrayList<OutputFormat>();
        for (OutputFormat outputFormat : OutputFormat.values()) {
            if (outputFormat != OutputFormat.WEBP) {
                expectedFormats.add(outputFormat);
            }
        }
        assertEquals(expectedFormats, instance.getSupportedOutputFormats());
    }

    public void testProcess() {
        // This is not easily testable in code, so will have to be tested by
        // human eyes.
    }

    public void testToString() {
        assertEquals("ImageMagickProcessor", instance.toString());
    }

    private File getFixture(String filename) throws IOException {
        File directory = new File(".");
        String cwd = directory.getCanonicalPath();
        Path testPath = Paths.get(cwd, "src", "test", "java", "edu",
                "illinois", "library", "cantaloupe", "test", "fixtures");
        return new File(testPath + File.separator + filename);
    }

}