package edu.illinois.library.cantaloupe.image;

/**
 * Encapsulates the parameters of an IIIF request.
 *
 * @see <a href="http://iiif.io/api/image/2.0/#image-request-parameters">IIIF
 *      Image API 2.0</a>
 */
public class Parameters {

    private Format format;
    private String identifier;
    private Quality quality;
    private Region region;
    private Rotation rotation;
    private Size size;

    /**
     * @param identifier From URI
     * @param region From URI
     * @param size From URI
     * @param rotation From URI
     * @param quality From URI
     * @param format From URI
     */
    public Parameters(String identifier, String region, String size,
                      String rotation, String quality, String format) {
        this.identifier = identifier;
        this.format = Format.valueOf(format.toUpperCase());
        this.quality = Quality.valueOf(quality.toUpperCase());
        this.region = Region.fromUri(region);
        this.rotation = Rotation.fromUri(rotation);
        this.size = Size.fromUri(size);
    }

    public Format getFormat() {
        return format;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Quality getQuality() {
        return quality;
    }

    public Region getRegion() {
        return region;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Size getSize() {
        return size;
    }

}
