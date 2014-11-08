package nanking.nimbus;

import nanking.nimbus.hotswap.HotSwapContext;
import nanking.nimbus.util.ContextLookup;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by nanking on 14-11-8.
 */
public final class Nimbus {

    public static Iterator<HotSwapContext> lookupContexts(final Iterable<File> repos) {
        return new ContextLookup(repos);
    }

    public static Iterator<HotSwapContext> lookupContexts(final String path) {
        return lookupContexts(new File(path));
    }

    public static Iterator<HotSwapContext> lookupContexts(final File file) {
        return lookupContexts(new File[] {file});
    }

    public static Iterator<HotSwapContext> lookupContexts(final File[] files) {
        return lookupContexts(Arrays.asList(files));
    }



}
