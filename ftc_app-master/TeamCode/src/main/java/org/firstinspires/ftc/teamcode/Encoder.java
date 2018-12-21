package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous(name="EncoderTest", group="Template")
@Disabled
public class Encoder extends LinearOpMode {
    //private Robot robot = new Robot(this);
    //private boolean loop = true;
    private DcMotor lift;
    private DcMotor PBMotor;
    private DcMotor PFMotor;
    private DcMotor DBMotor;
    private DcMotor DFMotor;

    @Override
    public void runOpMode() {
        PBMotor = hardwareMap.get(DcMotor.class, "PB");
        PFMotor = hardwareMap.get(DcMotor.class, "PF");
        DBMotor = hardwareMap.get(DcMotor.class, "DB");
        DFMotor = hardwareMap.get(DcMotor.class, "DF");
        lift  = hardwareMap.get(DcMotor.class, "Back");

        PBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        PFMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        PBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        PFMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DFMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        PBMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PFMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DBMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DFMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int newPBTarget;
        int newPFTarget;
        int newDBTarget;
        int newDFTarget;

        //robot.init();

        //AutoTransitioner.transitionOnStop(this, "Teleop V1");

        waitForStart();
        //put autonomous code blocks here
        //robot.drive.timeDrive(.1,0,0,1);
        /*newPBTarget = PBMotor.getCurrentPosition() - 1440;
        newPFTarget = PBMotor.getCurrentPosition() - 1440;
        newDBTarget = PBMotor.getCurrentPosition() - 1440;
        newDFTarget = PBMotor.getCurrentPosition() - 1440;*/

        /*PBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        PFMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        DBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        DFMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/

        PBMotor.setPower(.2);
        PFMotor.setPower(.2);
        DBMotor.setPower(.2);
        DFMotor.setPower(.2);

        while (!isStopRequested()) {
            telemetry.addData("PB",PBMotor.getCurrentPosition());
            telemetry.addData("PF",PFMotor.getCurrentPosition());
            telemetry.addData("DB",DBMotor.getCurrentPosition());
            telemetry.addData("DF",DFMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
