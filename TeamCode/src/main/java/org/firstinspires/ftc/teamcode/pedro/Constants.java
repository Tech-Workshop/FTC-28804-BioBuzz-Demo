package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
           new PinpointLocalizer(h, localizerConfig),
           new Mecanum(h, drivetrainConfig),
           new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("drive_left_front");
        c.frontRightName.set("drive_right_front");
        c.backLeftName.set("drive_left_back");
        c.backRightName.set("drive_right_back");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(7.15947188730315);
        c.yPodOffset.set(-3.105287927342212);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
       c -> {
           Controller primaryTranslationalForward = Controller.proportional(0.21710396957886316);
           Controller secondaryTranslationalForward = Controller.proportional(0.08021411563448194);
           Controller primaryTranslationalLateral = Controller.proportional(0.29052865334922634);
           Controller secondaryTranslationalLateral = Controller.proportional(0.10734257434394716);

           c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
           c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

           c.coast.set(Controller.proportionalFeedforward(0.011499069295538813));
           c.brake.set(Controller.proportionalFeedforward(0.009774208901207991));

           c.headingFeedback.set(Controller.proportional(3.872783673697236));
           c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05588569784569803, 0.00516297121678146));

           c.linearBrakeCoefficients.set(Matrix.diag(0.10574474996281799, 0.06181904941503442));
           c.quadraticBrakeCoefficients.set(Matrix.diag(9.801562352395514E-4, 0.0013809654764440904));

           c.maxAchievableForwardVelocity.set(87.11971263823679);
           c.maxAchievableStrafeVelocity.set(71.07882072551813);
           c.naturalForwardDeceleration.set(30.913816642315098);
           c.naturalStrafeDeceleration.set(53.00054495969676);
       }
    );

}