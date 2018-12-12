package iot.system.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceTest {

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
    public void testReplyWithEmptyReadingIfNoTemperatureIsKnown() {
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));
        deviceActor.tell(new Device.ReadTemperature(42L), probe.testActor());
        Device.RespondTemperature response = probe.expectMsgClass(Device.RespondTemperature.class);
        assertThat(response.requestId).isEqualTo(42L);
        assertThat(response.value).isEmpty();
    }

    @Test
    public void testReplyWithLatestTemperatureReading() {
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

        deviceActor.tell(new Device.RecordTemperature(1L, 24.0), probe.testActor());
        assertThat(probe.expectMsgClass(Device.TemperatureRecorded.class).requestId).isEqualTo(1L);

        deviceActor.tell(new Device.ReadTemperature(2L), probe.testActor());
        Device.RespondTemperature response1 = probe.expectMsgClass(Device.RespondTemperature.class);
        assertThat(response1.requestId).isEqualTo(2L);
        assertThat(response1.value).isEqualTo(Optional.of(24.0));

        deviceActor.tell(new Device.RecordTemperature(3L, 55.0), probe.testActor());
        assertThat(probe.expectMsgClass(Device.TemperatureRecorded.class).requestId).isEqualTo(3L);

        deviceActor.tell(new Device.ReadTemperature(4L), probe.testActor());
        Device.RespondTemperature response2 = probe.expectMsgClass(Device.RespondTemperature.class);
        assertThat(response2.requestId).isEqualTo(4L);
        assertThat(response2.value).isEqualTo(Optional.of(55.0));
    }

    @Test
    public void testReplyToRegistrationRequests() {
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

        deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "device"), probe.testActor());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        assertThat(probe.lastSender()).isEqualTo(deviceActor);
    }

    @Test
    public void testIgnoreWrongRegistrationRequests() {
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

        deviceActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device"), probe.testActor());
        probe.expectNoMessage();

        deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "wrongDevice"), probe.testActor());
        probe.expectNoMessage();
    }


}
