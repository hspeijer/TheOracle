package net.destinylounge.ola.rpc;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/22/11
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRpcController implements RpcController {
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean failed() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String errorText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startCancel() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFailed(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isCanceled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void notifyOnCancel(RpcCallback<Object> objectRpcCallback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
