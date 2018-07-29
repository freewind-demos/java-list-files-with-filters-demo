package demo;

import com.github.freewind.lostlist.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class Hello {

    public static final File root = new File(".");

    public static void main(String[] args) {
        filterByExtensions(root);
        filterByDirAndFileFilters(root);
    }

    private static void filterByExtensions(File root) {
        System.out.println("------------ filterByExtensions ------------");
        Collection<File> files = FileUtils.listFiles(root, new String[]{"java", "xml"}, true);
        printFiles(files);
    }

    private static void filterByDirAndFileFilters(File root) {
        System.out.println("------------- filterByDirAndFileFilters -------------");
        Collection<File> files = FileUtils.listFiles(root, new SuffixFileFilter(new String[]{"java", "xml"}),
                new ExcludeDirFilter(root, new String[]{".idea", ".git"}));
        printFiles(files);
    }

    private static void printFiles(Collection<File> files) {
        for (File file : files) {
            System.out.println(file);
        }
    }
}

class ExcludeDirFilter extends DirectoryFileFilter {
    private final List<File> excludeDirs;

    public ExcludeDirFilter(File root, String[] excludes) {
        this.excludeDirs = buildExcludeFiles(root, excludes);
    }

    private List<File> buildExcludeFiles(File root, String[] excludes) {
        List<File> result = Lists.emptyArrayList();
        for (String exclude : excludes) {
            result.add(new File(root, exclude));
        }
        return result;
    }

    @Override
    public boolean accept(File file) {
        return super.accept(file) && !inExcludedDirs(file);
    }

    private boolean inExcludedDirs(File file) {
        if (file.isFile()) return true;

        for (File exclude : excludeDirs) {
            if (getAllParentDirs(file).contains(exclude)) {
                return true;
            }
        }
        return false;
    }

    private static List<File> getAllParentDirs(File file) {
        List<File> result = Lists.list();
        File parent = file.isFile() ? file.getParentFile() : file;
        while (true) {
            if (parent == null) {
                break;
            }
            result.add(parent);
            parent = parent.getParentFile();
        }
        return result;
    }

}