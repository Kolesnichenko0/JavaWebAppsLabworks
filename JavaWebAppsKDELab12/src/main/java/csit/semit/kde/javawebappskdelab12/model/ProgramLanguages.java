package csit.semit.kde.javawebappskdelab12.model;

public enum ProgramLanguages {
    java("Java"),
    net(".NET"),
    php("PHP"),
    python("Python");

    private final String displayName;

    ProgramLanguages(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String[] getProgLangs() {
        ProgramLanguages[] pl = values();
        String[] plNames = new String[pl.length];
        for (int i = 0; i < pl.length; i++) {
            plNames[i] = pl[i].getDisplayName();
        }
        return plNames;
    }

    public static int getEnumIndex(String value) {
        int index = -1;
        ProgramLanguages[] pl = values();
        for (ProgramLanguages lang: pl) {
            index++;
            if (lang.getDisplayName().equals(value)) {
                break;
            }
        }
        return index;
    }


}
