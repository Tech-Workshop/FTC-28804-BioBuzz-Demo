package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "DEMO",name="Demo")

public class Demo extends LinearOpMode {
	private DcMotorEx leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intake,shooter,shooterExtender;
	private Servo intakeRetractorLeft,intakeRetractorRight,shooterAngle;
	private CRServo indexerLeft,indexerRight;

	private double shooterAnglePos=0.50;
	private int shooterExtensionPos=0;
	private boolean shooterExtenderMoving=false;
	private ElapsedTime shooterDelay= new ElapsedTime();

	@Override
	public void runOpMode() throws InterruptedException {
		//Drivetrain motors
		leftFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_FRONT_ID);
		leftBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_LEFT_BACK_ID);
		rightFrontDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_FRONT_ID);
		rightBackDrive = hardwareMap.get(DcMotorEx.class, Constants.Drive.DRIVE_RIGHT_BACK_ID);

		leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		leftFrontDrive.setDirection(Constants.Drive.DRIVE_LEFT_FRONT_DIRECTION);
		leftBackDrive.setDirection(Constants.Drive.DRIVE_LEFT_BACK_DIRECTION);
		rightFrontDrive.setDirection(Constants.Drive.DRIVE_RIGHT_FRONT_DIRECTION);
		rightBackDrive.setDirection(Constants.Drive.DRIVE_RIGHT_BACK_DIRECTION);

		//Intake motors
		intake = hardwareMap.get(DcMotorEx.class, Constants.Intake.INTAKE_ID);
		intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		intake.setDirection(Constants.Intake.INTAKE_DIRECTION);

		//Shooter motor
		shooter = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_ID);
		shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		shooter.setDirection(Constants.Shooter.SHOOTER_DIRECTION);

		//Shooter extender motor
		shooterExtender = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_EXTENDER_ID);
		shooterExtender.setDirection(Constants.Shooter.SHOOTER_EXTENDER_DIRECTION);
		shooterExtender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		shooterExtender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		//Shooter angle servo
		shooterAngle = hardwareMap.get(Servo.class, Constants.Shooter.SHOOTER_ANGLE_ID);

		//Intake retraction servos
		intakeRetractorLeft = hardwareMap.get(Servo.class, Constants.Intake.INTAKE_RETRACTOR_LEFT);
		intakeRetractorRight = hardwareMap.get(Servo.class, Constants.Intake.INTAKE_RETRACTOR_RIGHT);

		//Indexing servos
		indexerLeft = hardwareMap.get(CRServo.class, Constants.Indexer.INDEXER_LEFT_ID);
		indexerRight = hardwareMap.get(CRServo.class, Constants.Indexer.INDEXER_RIGHT_ID);

		//Initialize servo positions
		initializeServos();

		waitForStart();

		while (opModeIsActive() && !isStopRequested()) {
			//------------------------------------------
			//DRIVE

			float drive = -gamepad1.left_stick_y;  // Note: Motor direction is reversed, so negative y is forward
			float strafe = gamepad1.left_stick_x;
			float turn = gamepad1.right_stick_x;

			double frontLeftPower = drive + strafe + turn;
			double backLeftPower = drive - strafe + turn;
			double frontRightPower = drive - strafe - turn;
			double backRightPower = drive + strafe - turn;

			//Normalize values
			double max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
			max = Math.max(max, Math.abs(frontRightPower));
			max = Math.max(max, Math.abs(backRightPower));

			if (max > 1.0) {
				frontLeftPower /= max;
				backLeftPower /= max;
				frontRightPower /= max;
				backRightPower /= max;
			}

			//Slow-mo
			if (gamepad1.left_bumper){
				frontLeftPower *= 0.2;
				backLeftPower *= 0.2;
				frontRightPower *= 0.2;
				backRightPower *= 0.2;
			}

			// Send calculated power to wheels
			leftFrontDrive.setPower(frontLeftPower);
			leftBackDrive.setPower(backLeftPower);
			rightFrontDrive.setPower(frontRightPower);
			rightBackDrive.setPower(backRightPower);

			//------------------------------------------
			//SHOOTER (Angle)

			if(gamepad1.dpad_up || gamepad1.dpad_down) {
				if (gamepad1.dpad_left && shooterAnglePos<Constants.Shooter.SHOOTER_ANGLE_DOWN) {
					shooterAnglePos += 0.005;
				} else if (gamepad1.dpad_right && shooterAnglePos>Constants.Shooter.SHOOTER_ANGLE_UP) {
					shooterAnglePos -= 0.005;
				}

				shooterAngle.setPosition(shooterAnglePos);
			}

			telemetry.addLine("Shooter angle: "+shooterAnglePos);

			//------------------------------------------
			//SHOOTER (Extension)

			if(gamepad1.dpad_left || gamepad1.dpad_right) {
				if (gamepad1.dpad_left && shooterExtensionPos>Constants.Shooter.SHOOTER_EXTENDER_OUT) {
					shooterExtensionPos -= 5;
				} else if (gamepad1.dpad_right && shooterExtensionPos<Constants.Shooter.SHOOTER_EXTENDER_IN) {
					shooterExtensionPos += 5;
				}

				shooterExtender.setTargetPosition(shooterExtensionPos);
				shooterExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
				shooterExtender.setPower(0.5);
				shooterExtenderMoving=true;
				telemetry.addLine("SHOOTER EXTENSION PRESSED");
			} else if(shooterExtenderMoving && !shooterExtender.isBusy()) {
				shooterExtender.setPower(0.0);
				shooterExtenderMoving=false;
			}

			telemetry.addLine("Shooter extension (actual): "+shooterExtender.getCurrentPosition());
			telemetry.addLine("Shooter extension: "+shooterExtensionPos);

			//------------------------------------------
			//SHOOTER
			if(gamepad1.right_trigger>0.05) {
				if(shooter.getPower()==0.0) {
					shooterDelay.reset();
				}

				shooter.setPower(1.0);

				if(shooterDelay.seconds()>1.0) {
					indexerLeft.setPower(1.0);
					indexerRight.setPower(-1.0);
				}
			} else {
				shooter.setPower(0.0);
				indexerLeft.setPower(0.0);
				indexerRight.setPower(0.0);
			}

			//------------------------------------------
			//INTAKE
			if(gamepad1.left_trigger>0.05) {
				intake.setPower(-0.80);
			} else {
				intake.setPower(0.0);
			}

			//------------------------------------------
			//INTAKE RETRACTION

			//Extend intake subassembly
			if(gamepad1.a) {
				intakeRetractorLeft.setPosition(Constants.Intake.INTAKE_RETRACTOR_EXTENDED_POS);
				intakeRetractorRight.setPosition(Constants.Intake.INTAKE_RETRACTOR_EXTENDED_POS);

			//Retract intake subassembly
			} else if(gamepad1.b) {
				intakeRetractorLeft.setPosition(Constants.Intake.INTAKE_RETRACTOR_RETRACTED_POS);
				intakeRetractorRight.setPosition(Constants.Intake.INTAKE_RETRACTOR_RETRACTED_POS);
			}

			telemetry.update();
		}
	}

	public void initializeServos() {
		shooterAngle.setPosition(Constants.Shooter.SHOOTER_ANGLE_SHOOT);
		intakeRetractorLeft.setPosition(Constants.Intake.INTAKE_RETRACTOR_RETRACTED_POS);
		intakeRetractorRight.setPosition(Constants.Intake.INTAKE_RETRACTOR_RETRACTED_POS);
	}
}


