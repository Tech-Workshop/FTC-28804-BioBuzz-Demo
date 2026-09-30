package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

/*
@TeleOp(group = "UTILITY",name="Utility: Shooter kP")
public class UtilityShooterTunerKp extends OpMode {

	private DcMotorEx shooter;
	double[] increments = { 0.000001, 0.00001, 0.0001, 0.001, 0.01 };
	int incrementIndex = 2;
	double kS=0.09;
	double kV=0.00035;
	public static double kP=0.0;
	double goalVelocity=1730;
	FtcDashboard dashboard=FtcDashboard.getInstance();
	Telemetry d_telemetry=dashboard.getTelemetry();

	@Override
	public void init() {
		shooter = hardwareMap.get(DcMotorEx.class, Constants.Shooter.SHOOTER_ID);
		shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		shooter.setDirection(Constants.Shooter.SHOOTER_DIRECTION);
	}

	@Override
	public void loop() {
		if(gamepad1.dpadRightWasPressed() && incrementIndex<4) {
			incrementIndex++;
		} else if(gamepad1.dpadLeftWasPressed() && incrementIndex>0) {
			incrementIndex--;
		}

		double currentStep=increments[incrementIndex];

		if(gamepad1.dpadUpWasPressed()) { kP += currentStep; }
		if(gamepad1.dpadDownWasPressed()) { kP -= currentStep; }

		if(gamepad1.a) {
			goalVelocity=1730;
		} else if(gamepad1.b) {
			goalVelocity=800;
		} else if(gamepad1.x) {
			goalVelocity=1200;
		}

		double feedForward=(kV*goalVelocity)+kS;
		double error=goalVelocity-shooter.getVelocity();
		double power=(error*kP)+feedForward;

		shooter.setPower(power);

		telemetry.addData("kP","%.6f",kP);
		telemetry.addData("Power","%.3f",power);
		telemetry.addData("Flywheel Goal",goalVelocity);
		telemetry.addData("Flywheel Actual",shooter.getVelocity());
		telemetry.addData("Increment Index",incrementIndex);
		telemetry.addData("Increment","%.6f",increments[incrementIndex]);
		telemetry.update();

		d_telemetry.addData("Zero Line",0.0);
		d_telemetry.addData("Velocity (Goal)",goalVelocity);
		d_telemetry.addData("Velocity (Actual)",shooter.getVelocity());
		d_telemetry.update();
	}
}
*/