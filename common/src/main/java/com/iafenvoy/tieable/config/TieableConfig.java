package com.iafenvoy.tieable.config;

public class TieableConfig {
    public static final String PATH = "./config/tieable.json";
    public static final TieableConfig INSTANCE = ConfigLoader.load(TieableConfig.class, PATH, new TieableConfig());
    public boolean shearsUntieOnBlocks = true, shearsUntieOnItems = true;
}
