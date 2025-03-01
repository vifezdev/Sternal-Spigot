package me.suicidalkids.ion.visuals;

import net.minecraft.server.NBTTagCompound;

import java.util.EnumMap;

public class VisualSettings implements Visuals {

    private final EnumMap<VisualType, Boolean> settings = new EnumMap<>(VisualType.class);

    public VisualSettings() {
        for (VisualType  visualType: VisualType.values()) {
            settings.put(visualType, visualType.getDefault());
        }
    }

    @Override
    public void toggle(VisualType visualType) {
        setEnabled(visualType, !isEnabled(visualType));
    }

    @Override
    public void setEnabled(VisualType visualType, boolean enabled) {
        settings.put(visualType, enabled);
    }

    @Override
    public boolean isEnabled(VisualType visualType) {
        return settings.get(visualType);
    }

    @Override
    public boolean isToggled(VisualType visualType) {
        return !(settings.get(visualType) && visualType.getDefault());
    }

    public void loadCompound(NBTTagCompound nbtTagCompound) {
        for (VisualType visualType : VisualType.values()) {
            if (nbtTagCompound.hasKey(visualType.name())) {
                setEnabled(visualType, nbtTagCompound.getBoolean(visualType.name()));
            }
        }
    }

    public NBTTagCompound getCompound() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        for (VisualType visualType : VisualType.values())
            nbtTagCompound.setBoolean(visualType.name(), isEnabled(visualType));
        return nbtTagCompound;
    }

}
