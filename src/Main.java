import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress save1 = new GameProgress(1, 2, 3, 4);
        GameProgress save2 = new GameProgress(5, 2, 40, 4);
        GameProgress save3 = new GameProgress(5, 25, 40, 6);
        saveGame("C:\\IdeaProjects\\Games\\savegames\\save1.dat", save1);
        saveGame("C:\\IdeaProjects\\Games\\savegames\\save2.dat", save2);
        saveGame("C:\\IdeaProjects\\Games\\savegames\\save3.dat", save3);
        zipFiles("C:\\IdeaProjects\\Games\\savegames\\save.zip",
                Arrays.asList("C:\\IdeaProjects\\Games\\savegames\\save1.dat",
                        "C:\\IdeaProjects\\Games\\savegames\\save2.dat",
                        "C:\\IdeaProjects\\Games\\savegames\\save3.dat"));
        openZip("C:\\IdeaProjects\\Games\\savegames\\save.zip", "C:\\IdeaProjects\\Games\\savegames");
        System.out.println(openProgress("C:\\IdeaProjects\\Games\\savegames\\save1.dat"));
        System.out.println(openProgress("C:\\IdeaProjects\\Games\\savegames\\save2.dat"));
        System.out.println(openProgress("C:\\IdeaProjects\\Games\\savegames\\save3.dat"));
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String path, List<String> files) {
        String[] fileArr;
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    fileArr = file.split("\\\\");
                    ZipEntry entry = new ZipEntry(fileArr[fileArr.length - 1]);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                File notZipFile = new File(file);
                notZipFile.delete();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void openZip(String pathToZip, String pathToOpenZip) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathToZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(pathToOpenZip + "\\\\" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}