package nanking.nimbus.util;

import com.google.common.collect.Maps;
import nanking.nimbus.hotswap.HotSwapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nanking on 14-11-8.
 */
public class ContextLookup implements Iterator<HotSwapContext>{

    private static Logger logger = LoggerFactory.getLogger(ContextLookup.class);

    private final Map<File, HotSwapContext> contexts = Maps.newConcurrentMap();

    private final Iterator<HotSwapContext> iterator;

    public ContextLookup(final Iterable<File> files) {
       if (null == files){
           throw new IllegalArgumentException("files is null");
       }
       makeupContexts(files);
       iterator = contexts.values().iterator();
    }

    private void makeupContexts(final Iterable<File> files) {
       for (File file : files) {
            if (file.exists()) {
                makeupContexts(file);
            }
       }
    }

    private void makeupContexts(final File file) {
        if (!file.isDirectory()) {
            logger.info("file path is not valid,it should be {name}/{version}/{name}-{version}.jar");
            return;
        }

        final Pattern namePattern = Pattern.compile("^[a-zA-Z0-9\\-_\\@~#]+\"");
        final File[] dirs = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                Matcher matcher = namePattern.matcher(name);
                if (matcher.find()) {
                    logger.info("find module name {} ", name);
                    return true;
                }
                return false;
            }
        }) ;

        if (null == dirs && dirs.length > 0) {
            for (File module : dirs) {
                if (!module.isDirectory()) {
                    logger.warn("illegal module directory was found {}", module);
                    continue;
                }

                final String name = module.getName();
                final File[] versionDirs = module.listFiles();
                for (File versionDir : versionDirs) {
                    makeupContexts(name, versionDir);
                }
            }
        }


    }

    private void makeupContexts(final String name, final File versionDir) {
        if (!versionDir.isDirectory()) {
            return;
        }
        final String jarName = name + "-" + versionDir.getName() + ".jar";
        final File target = new File(versionDir, jarName);
        if (null != target && target.exists()) {
            contexts.put(target, new HotSwapContext(target, getManifest(target)));
        }
    }


    public static Manifest getManifest(final File file) {
        JarFile jarEntry = null;
        try {
            jarEntry = new JarFile(file);
            if (null != jarEntry) {
               return jarEntry.getManifest();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (null != jarEntry) {
                try {
                    jarEntry.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public HotSwapContext next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
