package iot.system.actors;

import akka.actor.AbstractActor;

/**
 * DeviceManager
 * @author ging wu
 * @date 2018/12/12
 */
public class DeviceManager extends AbstractActor {

    public static final class RequestTrackDevice {
        public final String groupId;
        public final String deviceId;

        public RequestTrackDevice(String groupId, String deviceId) {
            this.groupId = groupId;
            this.deviceId = deviceId;
        }
    }

    public static final class DeviceRegistered {
    }


    @Override
    public Receive createReceive() {
        return null;
    }
}
