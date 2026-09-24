package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(group = "DEMO",name="Demo (v2)")
public class Demo extends LinearOpMode {
	private DcMotorEx leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intake,shooter;
	private Servo intakeRetractorLeft,intakeRetractorRight,ballStop;
	private CRServo indexerLeft,indexerRight;

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

		//Ball stop servo
		ballStop = hardwareMap.get(Servo.class, Constants.Shooter.BALL_STOP_ID);

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
			//SHOOTER
			if(gamepad1.right_trigger>0.05) {
				if(shooter.getPower()==0.0) {
					shooterDelay.reset();
				}

				shooter.setPower(1.0);

				if(shooterDelay.seconds()>1.0) {
					ballStop.setPosition(Constants.Shooter.BALL_STOP_UP);
					indexerLeft.setPower(1.0);
					indexerRight.setPower(-1.0);
				}
			} else {
				shooter.setPower(0.0);
				ballStop.setPosition(Constants.Shooter.BALL_STOP_DOWN);
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
		ballStop.setPosition(Constants.Shooter.BALL_STOP_DOWN);
		intakeRetractorLeft.setPosition(Constants.Intake.INTAKE_RETRACTOR_RETRACTED_POS);
		intakeRetractorRight.setPosition(Constants.Intake.INTAKE_RETRACTOR_RETRACTED_POS);
	}
}


