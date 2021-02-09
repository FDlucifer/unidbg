package com.github.unidbg.arm.backend;

import com.github.unidbg.Emulator;
import com.github.unidbg.arm.backend.kvm.Kvm;
import com.github.unidbg.arm.backend.kvm.KvmCallback;
import com.github.unidbg.arm.backend.kvm.KvmException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class KvmBackend extends FastBackend implements Backend, KvmCallback {

    private static final Log log = LogFactory.getLog(KvmBackend.class);

    protected final Kvm kvm;

    protected KvmBackend(Emulator<?> emulator, Kvm kvm) throws BackendException {
        super(emulator);
        this.kvm = kvm;
        int maxSlots = Kvm.getMaxSlots();
        if (log.isDebugEnabled()) {
            log.debug("init kvm backend kvm=" + kvm + ", maxSlots=0x" + Integer.toHexString(maxSlots));
        }
        try {
            this.kvm.setKvmCallback(this);
        } catch (KvmException e) {
            throw new BackendException(e);
        }
    }

    @Override
    public final void destroy() throws BackendException {
        IOUtils.closeQuietly(kvm);
    }
}
