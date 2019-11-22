package hou.kot.iot;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class DeviceTest {

//    @ClassRule public static final TestKitJunitResource testKit = new TestKitJunitResource();
//
//    @Test
//    public void testReplyWithLatestTemperatureReading() {
//        TestProbe<Device.TemperatureRecorded> recordProbe =
//                testKit.createTestProbe(Device.TemperatureRecorded.class);
//        TestProbe<Device.RespondTemperature> readProbe =
//                testKit.createTestProbe(Device.RespondTemperature.class);
//        ActorRef<Device.Command> deviceActor = testKit.spawn(Device.create("group", "device"));
//
//        deviceActor.tell(new Device.RecordTemperature(1L, 24.0, recordProbe.getRef()));
//        assertEquals(1L, recordProbe.receiveMessage().requestId);
//
//        deviceActor.tell(new Device.ReadTemperature(2L, readProbe.getRef()));
//        Device.RespondTemperature response1 = readProbe.receiveMessage();
//        assertEquals(2L, response1.requestId);
//        assertEquals(Optional.of(24.0), response1.value);
//
//        deviceActor.tell(new Device.RecordTemperature(3L, 55.0, recordProbe.getRef()));
//        assertEquals(3L, recordProbe.receiveMessage().requestId);
//
//        deviceActor.tell(new Device.ReadTemperature(4L, readProbe.getRef()));
//        Device.RespondTemperature response2 = readProbe.receiveMessage();
//        assertEquals(4L, response2.requestId);
//        assertEquals(Optional.of(55.0), response2.value);
//    }
//
//    @Test
//    public void testReplyToRegistrationRequests() {
//        TestProbe<DeviceManager.DeviceRegistered> probe = testKit.createTestProbe(DeviceManager.DeviceRegistered.class);
//        ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device", probe.getRef()));
//        DeviceManager.DeviceRegistered registered1 = probe.receiveMessage();
//
//        // another deviceId
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device3", probe.getRef()));
//        DeviceManager.DeviceRegistered registered2 = probe.receiveMessage();
//        assertNotEquals(registered1.device, registered2.device);
//
//        // Check that the device actors are working
//        TestProbe<Device.TemperatureRecorded> recordProbe =
//                testKit.createTestProbe(Device.TemperatureRecorded.class);
//        registered1.device.tell(new Device.RecordTemperature(0L, 1.0, recordProbe.getRef()));
//        assertEquals(0L, recordProbe.receiveMessage().requestId);
//        registered2.device.tell(new Device.RecordTemperature(1L, 2.0, recordProbe.getRef()));
//        assertEquals(1L, recordProbe.receiveMessage().requestId);
//    }
//
//    @Test
//    public void testIgnoreWrongRegistrationRequests() {
//        TestProbe<DeviceManager.DeviceRegistered> probe = testKit.createTestProbe(DeviceManager.DeviceRegistered.class);
//        ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));
//        groupActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device1", probe.getRef()));
//        probe.expectNoMessage();
//    }
//
//    @Test
//    public void testListActiveDevices() {
//        TestProbe<DeviceManager.DeviceRegistered> registeredProbe = testKit.createTestProbe(DeviceManager.DeviceRegistered.class);
//        ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1", registeredProbe.getRef()));
//        registeredProbe.receiveMessage();
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2", registeredProbe.getRef()));
//        registeredProbe.receiveMessage();
//
//        TestProbe<DeviceManager.ReplyDeviceList> deviceListProbe = testKit.createTestProbe(DeviceManager.ReplyDeviceList.class);
//
//        groupActor.tell(new DeviceManager.RequestDeviceList(0L, "group", deviceListProbe.getRef()));
//        DeviceManager.ReplyDeviceList reply = deviceListProbe.receiveMessage();
//        assertEquals(0L, reply.requestId);
//        assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.ids);
//    }
//
//    @Test
//    public void testListActiveDevicesAfterOneShutsDown() {
//        TestProbe<DeviceManager.DeviceRegistered> registeredProbe = testKit.createTestProbe(DeviceManager.DeviceRegistered.class);
//        ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1", registeredProbe.getRef()));
//        DeviceManager.DeviceRegistered registered1 = registeredProbe.receiveMessage();
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2", registeredProbe.getRef()));
//        DeviceManager.DeviceRegistered registered2 = registeredProbe.receiveMessage();
//
//        ActorRef<Device.Command> toShutDown = registered1.device;
//
//        TestProbe<DeviceManager.ReplyDeviceList> deviceListProbe = testKit.createTestProbe(DeviceManager.ReplyDeviceList.class);
//
//        groupActor.tell(new DeviceManager.RequestDeviceList(0L, "group", deviceListProbe.getRef()));
//        DeviceManager.ReplyDeviceList reply = deviceListProbe.receiveMessage();
//        assertEquals(0L, reply.requestId);
//        assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.ids);
//
//        toShutDown.tell(Device.Passivate.INSTANCE);
//        registeredProbe.expectTerminated(toShutDown, registeredProbe.getRemainingOrDefault());
//
//
//
//        // using awaitAssert to retry because it might take longer for the groupActor
//        // to see the Terminated, that order is undefined
//        registeredProbe.awaitAssert(
//                () -> {
//                    groupActor.tell(new DeviceManager.RequestDeviceList(1L, "group", deviceListProbe.getRef()));
//                    DeviceManager.ReplyDeviceList r = deviceListProbe.receiveMessage();
//                    assertEquals(1L, r.requestId);
//                    assertEquals(Stream.of("device2").collect(Collectors.toSet()), r.ids);
//                    return null;
//                });
//    }
//
//    @Test
//    public void testReturnTemperatureValueForWorkingDevices() {
//        TestProbe<DeviceManager.RespondAllTemperatures> requester =
//                testKit.createTestProbe(DeviceManager.RespondAllTemperatures.class);
//        TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
//        TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);
//
//        Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
//        deviceIdToActor.put("device1", device1.getRef());
//        deviceIdToActor.put("device2", device2.getRef());
//
//        ActorRef<DeviceGroupQuery.Command> queryActor =
//                testKit.spawn(
//                        DeviceGroupQuery.create(
//                                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));
//
//        device1.expectMessageClass(Device.ReadTemperature.class);
//        device2.expectMessageClass(Device.ReadTemperature.class);
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device2", Optional.of(2.0))));
//
//        DeviceManager.RespondAllTemperatures response = requester.receiveMessage();
//        assertEquals(1L, response.requestId);
//
//        Map<String, DeviceManager.TemperatureReading> expectedTemperatures = new HashMap<>();
//        expectedTemperatures.put("device1", new DeviceManager.Temperature(1.0));
//
//        expectedTemperatures.put("device2", new DeviceManager.Temperature(2.0));
//
//        assertEquals(expectedTemperatures, response.temperatures);
//    }
//
//    @Test
//    public void testReturnTemperatureNotAvailableForDevicesWithNoReadings() {
//        TestProbe<DeviceManager.RespondAllTemperatures> requester =
//                testKit.createTestProbe(DeviceManager.RespondAllTemperatures.class);
//        TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
//        TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);
//
//        Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
//        deviceIdToActor.put("device1", device1.getRef());
//        deviceIdToActor.put("device2", device2.getRef());
//
//        ActorRef<DeviceGroupQuery.Command> queryActor =
//                testKit.spawn(
//                        DeviceGroupQuery.create(
//                                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));
//
//        assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId);
//        assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId);
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device1", Optional.empty())));
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device2", Optional.of(2.0))));
//
//        DeviceManager.RespondAllTemperatures response = requester.receiveMessage();
//        assertEquals(1L, response.requestId);
//
//        Map<String, DeviceManager.TemperatureReading> expectedTemperatures = new HashMap<>();
//        expectedTemperatures.put("device1", DeviceManager.TemperatureNotAvailable.INSTANCE);
//        expectedTemperatures.put("device2", new DeviceManager.Temperature(2.0));
//
//        assertEquals(expectedTemperatures, response.temperatures);
//    }
//
//    @Test
//    public void testReturnDeviceNotAvailableIfDeviceStopsBeforeAnswering() {
//        TestProbe<DeviceManager.RespondAllTemperatures> requester =
//                testKit.createTestProbe(DeviceManager.RespondAllTemperatures.class);
//        TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
//        TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);
//
//        Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
//        deviceIdToActor.put("device1", device1.getRef());
//        deviceIdToActor.put("device2", device2.getRef());
//
//        ActorRef<DeviceGroupQuery.Command> queryActor =
//                testKit.spawn(
//                        DeviceGroupQuery.create(
//                                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));
//
//        assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId);
//        assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId);
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));
//
//        device2.stop();
//
//        DeviceManager.RespondAllTemperatures response = requester.receiveMessage();
//        assertEquals(1L, response.requestId);
//
//        Map<String, DeviceManager.TemperatureReading> expectedTemperatures = new HashMap<>();
//        expectedTemperatures.put("device1", new DeviceManager.Temperature(1.0));
//        expectedTemperatures.put("device2", DeviceManager.DeviceNotAvailable.INSTANCE);
//
//        assertEquals(expectedTemperatures, response.temperatures);
//    }
//
//
//    @Test
//    public void testReturnTemperatureReadingEvenIfDeviceStopsAfterAnswering() {
//        TestProbe<DeviceManager.RespondAllTemperatures> requester =
//                testKit.createTestProbe(DeviceManager.RespondAllTemperatures.class);
//        TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
//        TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);
//
//        Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
//        deviceIdToActor.put("device1", device1.getRef());
//        deviceIdToActor.put("device2", device2.getRef());
//
//        ActorRef<DeviceGroupQuery.Command> queryActor =
//                testKit.spawn(
//                        DeviceGroupQuery.create(
//                                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));
//
//        assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId);
//        assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId);
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device2", Optional.of(2.0))));
//
//        device2.stop();
//
//        DeviceManager.RespondAllTemperatures response = requester.receiveMessage();
//        assertEquals(1L, response.requestId);
//
//        Map<String, DeviceManager.TemperatureReading> expectedTemperatures = new HashMap<>();
//        expectedTemperatures.put("device1", new DeviceManager.Temperature(1.0));
//        expectedTemperatures.put("device2", new DeviceManager.Temperature(2.0));
//
//        assertEquals(expectedTemperatures, response.temperatures);
//    }
//
//    @Test
//    public void testReturnDeviceTimedOutIfDeviceDoesNotAnswerInTime() {
//        TestProbe<DeviceManager.RespondAllTemperatures> requester =
//                testKit.createTestProbe(DeviceManager.RespondAllTemperatures.class);
//        TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
//        TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);
//
//        Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
//        deviceIdToActor.put("device1", device1.getRef());
//        deviceIdToActor.put("device2", device2.getRef());
//
//        ActorRef<DeviceGroupQuery.Command> queryActor =
//                testKit.spawn(
//                        DeviceGroupQuery.create(
//                                deviceIdToActor, 1L, requester.getRef(), Duration.ofMillis(200)));
//
//        assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId);
//        assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId);
//
//        queryActor.tell(
//                new DeviceGroupQuery.WrappedRespondTemperature(
//                        new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));
//
//        // no reply from device2
//
//        DeviceManager.RespondAllTemperatures response = requester.receiveMessage();
//        assertEquals(1L, response.requestId);
//
//        Map<String, DeviceManager.TemperatureReading> expectedTemperatures = new HashMap<>();
//        expectedTemperatures.put("device1", new DeviceManager.Temperature(1.0));
//        expectedTemperatures.put("device2", DeviceManager.DeviceTimedOut.INSTANCE);
//
//        assertEquals(expectedTemperatures, response.temperatures);
//    }
//
//
//    @Test
//    public void testCollectTemperaturesFromAllActiveDevices() {
//        TestProbe<DeviceManager.DeviceRegistered> registeredProbe = testKit.createTestProbe(DeviceManager.DeviceRegistered.class);
//        ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1", registeredProbe.getRef()));
//        ActorRef<Device.Command> deviceActor1 = registeredProbe.receiveMessage().device;
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2", registeredProbe.getRef()));
//        ActorRef<Device.Command> deviceActor2 = registeredProbe.receiveMessage().device;
//
//        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device3", registeredProbe.getRef()));
//        ActorRef<Device.Command> deviceActor3 = registeredProbe.receiveMessage().device;
//
//        // Check that the device actors are working
//        TestProbe<Device.TemperatureRecorded> recordProbe =
//                testKit.createTestProbe(Device.TemperatureRecorded.class);
//        deviceActor1.tell(new Device.RecordTemperature(0L, 1.0, recordProbe.getRef()));
//        assertEquals(0L, recordProbe.receiveMessage().requestId);
//        deviceActor2.tell(new Device.RecordTemperature(1L, 2.0, recordProbe.getRef()));
//        assertEquals(1L, recordProbe.receiveMessage().requestId);
//        // No temperature for device 3
//
//        TestProbe<DeviceManager.RespondAllTemperatures> allTempProbe =
//                testKit.createTestProbe(DeviceManager.RespondAllTemperatures.class);
//        groupActor.tell(new DeviceManager.RequestAllTemperatures(0L, "group", allTempProbe.getRef()));
//        DeviceManager.RespondAllTemperatures response = allTempProbe.receiveMessage();
//        assertEquals(0L, response.requestId);
//
//        Map<String, DeviceManager.TemperatureReading> expectedTemperatures = new HashMap<>();
//        expectedTemperatures.put("device1", new DeviceManager.Temperature(1.0));
//        expectedTemperatures.put("device2", new DeviceManager.Temperature(2.0));
//        expectedTemperatures.put("device3", DeviceManager.TemperatureNotAvailable.INSTANCE);
//
//        assertEquals(expectedTemperatures, response.temperatures);
//    }
}