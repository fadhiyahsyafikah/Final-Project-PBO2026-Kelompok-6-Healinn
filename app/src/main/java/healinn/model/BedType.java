package healinn.model;

public enum BedType {

    TWIN_BED  ("Twin Bed",   "2 tempat tidur terpisah"),
    DOUBLE_BED("Double Bed", "1 tempat tidur besar");

    private final String displayName;
    private final String description;

    BedType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }

    public long getPriceFor(RoomType roomType) {
        return switch (roomType) {
            case STANDARD -> this == TWIN_BED ? 500_000 : 450_000;
            case DELUXE -> this == TWIN_BED ? 1_000_000 : 750_000;
            case SUITE -> this == TWIN_BED ? 1_200_000 : 1_000_000;
        };
    }

    public String getFormattedPrice(RoomType roomType) {
        return RoomType.formatRupiah(getPriceFor(roomType)) + "/malam";
    }
}