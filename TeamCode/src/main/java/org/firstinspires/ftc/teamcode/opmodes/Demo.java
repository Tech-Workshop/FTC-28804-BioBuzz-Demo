package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.utility.MathUtility.clamp;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.utility.MathUtility;

@TeleOp(group = "DEMO",name="Demo (v2)")
public class Demo extends LinearOpMode {
	private Follower follower;
	private DcMotorEx leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive,intake,shooter;
	private Servo intakeRetractorLeft,intakeRetractorRight,ballStop;
	private CRServo indexerLeft,indexerRight;

	private ElapsedTime shooterDelay= new ElapsedTime();

	//PP pose factory
	private final PoseFactory pose = PoseFactory.degrees();

	//Local variables
	private double headingTarget = 0.0, turnPower = 0.0;
	private final double[] autoCorrectAngles = {-Math.PI, -Math.PI * 3 / 4, -Math.PI / 2, -Math.PI / 4, 0.0, Math.PI / 4, Math.PI / 2, Math.PI * 3 / 4, Math.PI};

	@Override
	public void runOpMode() throws InterruptedException {
		//PedroPathing follower
		follower=org.firstinspires.ftc.teamcode.pedro.Constants.create(hardwareMap);
		follower.setPose(pose.of(84.75,132.7,270.0));
		follower.update();

		//Drivetrain motors
		/*
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
		*/

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

			/*
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
			*/

			DrivePowers powers = ManualDrive.fieldCentric(
				gamepad1.left_stick_y,
				gamepad1.left_stick_x,
				-gamepad1.right_stick_x,
				follower.pose().heading()
			);

			follower.manual(powers);
			follower.update();

			/*
			//Current robot heading
			double headingCurrent = MathUtility.normalizeAngle(follower.pose().heading());

			//Check for low-speed mode
			double SPEED_MULTIPLIER = 1.0;
			if (gamepad1.left_bumper) {
				SPEED_MULTIPLIER = 0.3;
			}

			//Low speed mode - Right analog stick does standard turn (not set target)
			if (gamepad1.left_bumper && Math.abs(gamepad1.right_stick_x) > Constants.General.CONTROLLER_DEADBAND) {
				headingTarget = follower.pose().heading();
				turnPower = gamepad1.right_stick_x;
			} else {
				//TODO: Turn to flower angle here (based on location and robot heading)

				//Get target heading if right analog stick is fully engaged
				if (Math.sqrt(gamepad1.right_stick_x * gamepad1.right_stick_x + gamepad1.right_stick_y * gamepad1.right_stick_y) > 0.90) {
					headingTarget = MathUtility.normalizeAngle(Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x) + Math.PI / 2);
				}

				//Autocorrect to nearest 45 degrees if not in slow mode (heading target always -pi to pi)
				if (Constants.General.CONTROLLER_AUTO_HEADING) {
					for (double autoCorrectAngle : autoCorrectAngles) {
						if (Math.abs(headingTarget - autoCorrectAngle) < Constants.General.CONTROLLER_AUTO_ERROR) {
							headingTarget = autoCorrectAngle;
							break;
						}
					}
				}

				//Identify error between current heading and maintain heading
				double headingError = MathUtility.normalizeAngle(headingTarget - headingCurrent);
				telemetry.addData("Heading (target, current, error)", "%.2f, %.2f, %.2f", headingTarget, headingCurrent, headingError);

				//Calculate heading correction power (note error in radians)
				if (Math.abs(headingError) < Constants.Drive.TURN_ERROR) {
					turnPower = 0.0;
				} else if (headingError > 1.5) {
					turnPower = 1;
				} else if (headingError < -1.5) {
					turnPower = -1;
				} else {
					turnPower = clamp((headingError / Math.PI) * Constants.Drive.TURN_POWER_CLOSE + (Math.signum(headingError) * Constants.Drive.TURN_POWER_FF), -1.0, 1.0);
				}

				turnPower *= Constants.Drive.TURN_POWER_MULTIPLIER;
				telemetry.addData("TURN ERROR:",headingError);
				telemetry.addData("TURN POWER:",turnPower);
			}

			//Pedro pathing field centric
			DrivePowers powers = ManualDrive.fieldCentric(
				-gamepad1.left_stick_y*SPEED_MULTIPLIER,
				gamepad1.left_stick_x*SPEED_MULTIPLIER,
				turnPower*SPEED_MULTIPLIER,
				follower.pose().heading()
			);

			ManualDrive.driveOrHold(follower, powers); //Keeps robot in position when sticks are released
			//follower.manual(powers); //Old method for applying power (does not lock when sticks released)
			follower.update();

			//Display pose to screen
			telemetry.addData("Robot X", follower.pose().x());
			telemetry.addData("Robot Y", follower.pose().y());
			telemetry.addData("Robot Heading", Math.toDegrees(headingCurrent));
*/

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


