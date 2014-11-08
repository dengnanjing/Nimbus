package nanking.nimbus.hotswap;

import com.google.inject.AbstractModule;

/**
 * Created by nanking on 14-11-8.
 */
public class HotSwapModule extends AbstractModule {

    private HotSwapContext context;

    public HotSwapModule(final HotSwapContext context) {
        super();
        if (context == null){
            throw new IllegalArgumentException("HotSwapContext is null");
        }
        this.context = context;
    }

    @Override
    protected void configure() {

    }

}
