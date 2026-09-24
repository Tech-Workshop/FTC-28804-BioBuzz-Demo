package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp(group = "UTILITY",name="Utility: Pinpoint Test")
public class UtilityPinpoint extends LinearOpMode {
	private GoBildaPinpointDriver pinpoint;

	@Override
	public void runOpMode() throws InterruptedException {

		pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

		configurePinpoint();
		pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {
			pinpoint.update();
			Pose2D pose2D = pinpoint.getPosition();

			telemetry.addData("X coordinate (IN)", pose2D.getX(DistanceUnit.INCH));
			telemetry.addData("Y coordinate (IN)", pose2D.getY(DistanceUnit.INCH));
			telemetry.addData("Heading angle (DEGREES)", pose2D.getHeading(AngleUnit.DEGREES));
			telemetry.update();
		}
	}

	public void configurePinpoint(){
		/*
		 *  Set the odometry pod positions relative to the point that you want the position to be measured from.
		 *
		 *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
		 *  Left of the center is a positive number, right of center is a negative number.
		 *
		 *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
		 *  Forward of center is a positive number, backwards is a negative number.
		 */
		pinpoint.setOffsets(-181.95, 71.5, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

		pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

		pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED,
			GoBildaPinpointDriver.EncoderDirection.REVERSED);

		pinpoint.resetPosAndIMU();
	}


}
