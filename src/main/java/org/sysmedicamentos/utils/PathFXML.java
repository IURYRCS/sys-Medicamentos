package org.sysmedicamentos.utils;

import java.nio.file.Paths;

public class PathFXML {
    public static String pathBase() {
        return Paths.get("C:\\Users\\Iury\\Desktop\\ENG. SOFTWARE\\POO3\\sys-Medicamentos\\src\\main\\java\\org\\sysmedicamentos\\view").toAbsolutePath().toString();
    }
}
