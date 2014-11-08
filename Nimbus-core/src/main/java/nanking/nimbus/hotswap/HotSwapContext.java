package nanking.nimbus.hotswap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.jar.Manifest;

/**
 * Created by nanking on 14-11-8.
 */
public class HotSwapContext {

    private static Logger logger = LoggerFactory.getLogger(HotSwapContext.class);

    private File file;

    private Manifest manifest;

    private final Map<String, Object> attributes;

    public HotSwapContext(final File file,final Manifest manifest) {
        if (file == null){
            throw new IllegalArgumentException("file is null");
        }
        if (manifest == null){
            throw new IllegalArgumentException("manifest is null");
        }
        this.file = file;
        this.manifest = manifest;

        this.attributes = null;
    }
}
