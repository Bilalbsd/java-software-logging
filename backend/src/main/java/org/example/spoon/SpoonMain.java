package org.example.spoon;
import spoon.Launcher;
public class SpoonMain {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setCommentEnabled(true);// Conserver les commentaires

        // Assurez-vous que ce répertoire existe
        launcher.addInputResource("src/main/java/org/example");// Changer le chemin si nécessaire

        launcher.addProcessor(new Logging());
        launcher.setSourceOutputDirectory("src/main/java");
        launcher.run();
    }
}
