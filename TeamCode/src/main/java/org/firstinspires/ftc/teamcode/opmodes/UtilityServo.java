package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "UTILITY",name="Utility: Servo Reset/Tuning")
public class UtilityServo extends LinearOpMode {
	private Servo intakeRetractorLeft,intakeRetractorRight,shooterAngle;
	private CRServo indexerLeft,indexerRight;

	double intakeRetractorDiff=0.0;
	double shooterAnglePos=0.50;
	double indexerPower=0.50;

	@Override
	public void runOpMode() throws InterruptedException {
		shooterAngle = hardwareMap.get(Servo.class, Constants.Shooter.SHOOTER_ANGLE_ID);
		intakeRetractorLeft = hardwareMap.get(Servo.class, Constants.Intake.INTAKE_RETRACTOR_LEFT);
		intakeRetractorRight = hardwareMap.get(Servo.class, Constants.Intake.INTAKE_RETRACTOR_RIGHT);

		indexerLeft = hardwareMap.get(CRServo.class, Constants.Indexer.INDEXER_LEFT_ID);
		indexerRight = hardwareMap.get(CRServo.class, Constants.Indexer.INDEXER_RIGHT_ID);

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {

			if(gamepad1.a) { shooterAngle.setPosition(0.50); }
			if(gamepad1.b) {
				intakeRetractorLeft.setPosition(0.50);
				intakeRetractorRight.setPosition(0.50);
			}

			//Shooter Angle - Tuning
			if (gamepad1.dpad_up) {
				if (shooterAnglePos < 1.0) {
					shooterAnglePos+=0.001;
					shooterAngle.setPosition(shooterAnglePos);
				}
			} else if (gamepad1.dpad_down) {
				if (shooterAnglePos > 0.0) {
					shooterAnglePos-=0.001;
					shooterAngle.setPosition(shooterAnglePos);
				}
			}

			//Intake Retractors - Tuning
			if (gamepad1.dpad_left) {
				if (intakeRetractorDiff < 0.50) {
					intakeRetractorDiff+=0.001;
					intakeRetractorLeft.setPosition(0.50+intakeRetractorDiff);
					intakeRetractorRight.setPosition(0.50+intakeRetractorDiff);
				}
			} else if (gamepad1.dpad_right) {
				if (intakeRetractorDiff > -0.50) {
					intakeRetractorDiff-=0.001;
					intakeRetractorLeft.setPosition(0.50+intakeRetractorDiff);
					intakeRetractorRight.setPosition(0.50+intakeRetractorDiff);
				}
			}

			//Indexers
			if(gamepad1.rightBumperWasPressed()) { indexerPower+=0.01; }
			if(gamepad1.leftBumperWasPressed()) { indexerPower-=0.01; }
			if(gamepad1.right_trigger>0.05) {
				indexerLeft.setPower(indexerPower);
				indexerRight.setPower(-indexerPower);
			} else if(gamepad1.left_trigger>0.05) {
				indexerLeft.setPower(-indexerPower);
				indexerRight.setPower(indexerPower);
			} else {
				indexerLeft.setPower(0.0);
				indexerRight.setPower(0.0);
			}

			telemetry.addLine("Shooter Angle (Reset 50%) - Press A");
			telemetry.addLine("Shooter Angle (Tuning) - D-Pad Up/Down");
			telemetry.addData("Shooter Angle (Pos):",shooterAngle.getPosition());

			telemetry.addLine("Intake Retractors (Reset-50%) - Press B");
			telemetry.addLine("Intake Retractors (Tuning) - D-Pad Left/Right");
			telemetry.addData("Intake Retractors (Left Pos):",intakeRetractorLeft.getPosition());
			telemetry.addData("Intake Retractors (Left Pos):",intakeRetractorRight.getPosition());

			telemetry.addData("Indexers Power (Bumpers)",indexerPower);
			telemetry.addLine("Indexers (Move) - Triggers");

			telemetry.update();
		}
	}
}