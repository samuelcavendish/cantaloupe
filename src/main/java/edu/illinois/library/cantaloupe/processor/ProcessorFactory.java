package edu.illinois.library.cantaloupe.processor;

import edu.illinois.library.cantaloupe.config.ConfigurationFactory;
import edu.illinois.library.cantaloupe.operation.Format;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to obtain an instance of a {@link Processor} for a given source format,
 * as defined in the configuration.
 */
public abstract class ProcessorFactory {

    public static final String FALLBACK_PROCESSOR_CONFIG_KEY =
            "processor.fallback";

    public static Set<Processor> getAllProcessors() {
        return new HashSet<Processor>(Arrays.asList(
                new FfmpegProcessor(),
                new GraphicsMagickProcessor(),
                new ImageMagickProcessor(),
                new JaiProcessor(),
                new Java2dProcessor(),
                new KakaduProcessor(),
                new OpenJpegProcessor(),
                new PdfBoxProcessor()));
    }

    /**
     * Retrieves the best-match processor for the given source format. Its
     * source will not be set.
     *
     * @param sourceFormat Source format for which to retrieve a processor
     * @return An instance suitable for handling the given source format, based
     *         on configuration settings. The source is already set.
     * @throws UnsupportedSourceFormatException
     * @throws ReflectiveOperationException
     * @throws IOException
     */
    public static Processor getProcessor(final Format sourceFormat)
            throws UnsupportedSourceFormatException,
            ReflectiveOperationException,
            IOException {
        String processorName = getAssignedProcessorName(sourceFormat);
        if (processorName == null) {
            processorName = getFallbackProcessorName();
            if (processorName == null) {
                throw new ClassNotFoundException("A fallback processor is not defined.");
            }
        }
        final String className = ProcessorFactory.class.getPackage().getName() +
                "." + processorName;
        final Class class_ = Class.forName(className);
        final Processor processor = (Processor) class_.newInstance();

        processor.setSourceFormat(sourceFormat);

        return processor;
    }

    /**
     * @param format
     * @return Name of the processor assigned to the given format, or null if
     *         one is not set.
     */
    private static String getAssignedProcessorName(Format format) {
        final String value = ConfigurationFactory.getInstance().
                getString("processor." + format.getPreferredExtension());
        return (value != null && value.length() > 0) ? value : null;
    }

    private static String getFallbackProcessorName() {
        return ConfigurationFactory.getInstance().
                getString(FALLBACK_PROCESSOR_CONFIG_KEY);
    }

}
