package me.suicidalkids.ion.visuals;

/**
 * Visual Settings, this is used to keep track of the visuals the player has enabled.
 */
public interface Visuals {

    /**
     * Toggles the provided visual type.
     *
     * @param visualType the visual type
     */
    void toggle(VisualType visualType);

    /**
     * Toggles the provided visual type.
     *
     * @param visualType the visual type
     */
    void setEnabled(VisualType visualType, boolean enabled);

    /**
     * Checks if the provided visual type is enabled.
     *
     * @param visualType the visual type
     * @return if the visual type is enabled.
     */
    boolean isEnabled(VisualType visualType);

    /**
     * Checks if the setting of the provided visual type has been modified.
     *
     * @param visualType the visual type
     * @return if the setting of the visual type has been modified.
     */
    boolean isToggled(VisualType visualType);

    enum VisualType {

        TNT_VISIBILITY(true),
        SAND_VISIBILITY(true),
        FLASHING_TNT(true),
        ;

        private final boolean defaultValue;

        VisualType(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean getDefault() {
            return defaultValue;
        }

    }

}
