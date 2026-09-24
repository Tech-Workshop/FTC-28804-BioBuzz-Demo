package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import static com.pedropathing.api.Paths.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name="Auto (Demo)")
public class AutoDemo extends OpMode {
	private Follower follower;
	private final PoseFactory poseFactory = PoseFactory.degrees();

	private final Pose start = poseFactory.of(84.7384, 132.6954, 270);
	private final Pose path1 = poseFactory.of(84.7384, 124.4071, 270);
	private final Pose point2 = poseFactory.of(84.5757, 99.4871, 90);
	private final Pose point3 = poseFactory.of(84.7014, 124.5542, 90);
	private final Pose point4 = poseFactory.of(84.4725, 124.4845, 270);
	private final Pose point5 = poseFactory.of(129.599, 50.3907, -78.4844);
	private final Pose point5Control1 = poseFactory.of(118.975, 103.2806, 0);

	public Path path1() {
		return line(start, path1).linear(start, path1);
	}

	public Path path2() {
		return line(path1, point2).linear(path1, point2);
	}

	public Path path3() {
		return line(point2, point3).linear(point2, point3);
	}

	public Path path4() {
		return line(point3, point4).linear(point3, point4);
	}

	public Path path5() {
		return curve(point4, point5Control1, point5).tangent();
	}

	private Command autoRoutine() {
		//TODO: Add mechanism actions inline below

		return sequential(
			follow(follower, path1()),
			waitMs(2000),
			follow(follower, path2()),
			follow(follower, path3()),
			follow(follower, path4()),
			waitMs(2000),
			follow(follower, path5())
			);
	}

	@Override
	public void init() {
		Scheduler.reset();

		follower = Constants.create(hardwareMap);
		follower.setPose(start);
		follower.update();
	}

	@Override
	public void start() {
		schedule(autoRoutine());
	}

	@Override
	public void loop() {
		follower.update();
		Scheduler.execute();

		//TODO: add your other methods needed in the loop here

		telemetry.addData("X", follower.pose().x());
		telemetry.addData("Y", follower.pose().y());
		telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
		telemetry.addData("Follower Mode", follower.mode());
		telemetry.update();
	}
}
