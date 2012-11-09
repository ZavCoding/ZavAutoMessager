package com.feildmaster.lib.configuration;

import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.*;

public class EnhancedRepresenter extends YamlRepresenter {
    public EnhancedRepresenter() {
        super();
        this.nullRepresenter = new NullRepresenter();
    }

    class NullRepresenter implements Represent {
        @Override
        public Node representData(Object data) {
            return representScalar(Tag.NULL, "");
        }
    }
}
