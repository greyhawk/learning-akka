package iot.system.actors;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceGroupTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system, Duration.create("10 second"), false);
    }

    @Test
    public void testRegisterDeviceActor() {
        TestKit probe = new TestKit(system);
        ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        ActorRef deviceActor1 = probe.lastSender();

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        ActorRef deviceActor2 = probe.lastSender();
        assertThat(deviceActor1).isNotEqualTo(deviceActor2);

        // Check that the device actors are working
        deviceActor1.tell(new Device.RecordTemperature(0L, 1.0), probe.testActor());
        assertThat(probe.expectMsgClass(Device.TemperatureRecorded.class).requestId).isEqualTo(0L);
        deviceActor2.tell(new Device.RecordTemperature(1L, 2.0), probe.testActor());
        assertThat(probe.expectMsgClass(Device.TemperatureRecorded.class).requestId).isEqualTo(1L);
    }

    @Test
    public void testIgnoreRequestsForWrongGroupId() {
        TestKit probe = new TestKit(system);
        ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

        groupActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device1"), probe.testActor());
        probe.expectNoMessage();
    }

    @Test
    public void testReturnSameActorForSameDeviceId() {
        TestKit probe = new TestKit(system);
        ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        ActorRef deviceActor1 = probe.lastSender();

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        ActorRef deviceActor2 = probe.lastSender();
        assertThat(deviceActor1).isEqualTo(deviceActor2);
    }

    @Test
    public void testListActiveDevices() {
        TestKit probe = new TestKit(system);
        ActorRef groupActorRef = system.actorOf(DeviceGroup.props("group"));

        groupActorRef.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

        groupActorRef.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

        groupActorRef.tell(new DeviceGroup.RequestDeviceList(0L), probe.testActor());
        DeviceGroup.ReplyDeviceList reply = probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
        assertThat(reply.requestId).isEqualTo(0L);
        assertThat(reply.ids).contains("device1", "device2");
    }

    @Test
    public void testListActiveDevicesAfterOneShutsDown() {
        TestKit probe = new TestKit(system);
        ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        ActorRef toShutDown = probe.lastSender();

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

        groupActor.tell(new DeviceGroup.RequestDeviceList(0L), probe.testActor());
        DeviceGroup.ReplyDeviceList reply = probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
        assertThat(reply.requestId).isEqualTo(0L);
        assertThat(reply.ids).contains("device1", "device2");

        probe.watch(toShutDown);
        toShutDown.tell(PoisonPill.getInstance(), ActorRef.noSender());
        probe.expectTerminated(toShutDown, Duration.create("10 second"));

        // using awaitAssert to retry because it might take longer for the groupActor
        // to see the Terminated, that order is undefined
        probe.awaitAssert(() -> {
            groupActor.tell(new DeviceGroup.RequestDeviceList(1L), probe.testActor());
            DeviceGroup.ReplyDeviceList r =
                    probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
            assertThat(r.requestId).isEqualTo(1L);
            assertThat(reply.ids).contains("device2");
            return null;
        }, Duration.create("10 second"), Duration.create("100 millis"));
    }



}
