package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility: Drive Motors Test")
public class UtilityMotors extends LinearOpMode {
	private DcMotorEx leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive;

	@Override
	public void runOpMode() throws InterruptedException {
		leftFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_FRONT_ID);
		leftBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_BACK_ID);
		rightFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_FRONT_ID);
		rightBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_BACK_ID);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {
			leftFrontDrive.setPower(0.2);
			leftBackDrive.setPower(0.2);
			rightFrontDrive.setPower(0.2);
			rightBackDrive.setPower(0.2);
		}
	}
}