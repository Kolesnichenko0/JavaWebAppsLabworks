package csit.semit.kde.javawebappskdelab14.entity;

/**
 * The {@code ProgramLanguages} enum represents the programming languages that an {@link Employee} can specialize in.
 * Each enum constant is a programming language, and has a display name that is used for display purposes.
 * <p>
 * The enum provides methods to get the display names of all the programming languages, and to get the index of a programming language in the enum.
 * <p>
 * Original code provided by: Dvukhhlavov Dmytro Eduardovych
 * Changes made by: Your Name
 *
 * @author Dvukhhlavov Dmytro Eduardovych SEMIT department,
 * Kolesnychenko Denys Yevhenovych CS-222a
 * @see Employee
 * @see EmployeeList
 * @since 1.0.0
 */
public enum ProgramLanguages {
    java("Java"),
    net(".NET"),
    php("PHP"),
    python("Python");

    private final String displayName;

    ProgramLanguages(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the programming language.
     *
     * @return A string representing the display name of the programming language.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display names of all the programming languages in the enum.
     *
     * @return An array of strings representing the display names of all the programming languages in the enum.
     */
    public static String[] getProgLangs() {
        ProgramLanguages[] pl = values();
        String[] plNames = new String[pl.length];
        for (int i = 0; i < pl.length; i++) {
            plNames[i] = pl[i].getDisplayName();
        }
        return plNames;
    }

    /**
     * Returns the index of a programming language in the enum.
     *
     * @param value The display name of the programming language.
     * @return The index of the programming language in the enum, or -1 if the programming language is not in the enum.
     */
    public static int getEnumIndex(String value) {
        int index = -1;
        ProgramLanguages[] pl = values();
        for (ProgramLanguages lang : pl) {
            index++;
            if (lang.getDisplayName().equals(value)) {
                break;
            }
        }
        return index;
    }


}
