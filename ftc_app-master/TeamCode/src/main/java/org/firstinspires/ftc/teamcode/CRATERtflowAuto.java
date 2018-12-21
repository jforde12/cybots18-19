/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.List;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "CRATERtflowAuto", group = "Auto")

@Disabled

public class CRATERtflowAuto extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lift;
    private DcMotor PBMotor;
    private DcMotor PFMotor;
    private DcMotor DBMotor;
    private DcMotor DFMotor;
    private Servo MServo;

    int newPBTarget;
    int newPFTarget;
    int newDBTarget;
    int newDFTarget;

    private WebcamName Cam;

    /* 1
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = " AXxh0tb/////AAABmWb+78a+f0ZWp31duMgmIRqCXYNAMLawUpQhmgqk4qi8oCsSu56WI/frOwFakO9irsPzrh0OWL6gbuple+EVQG2hXwgcFuwsYKu/4kb3c7vPFa7ZVhQxTs6y675CyYuuXR0oz0rXKgmibrEV8qAOrTAlxDPgkMCeVBiM+M4y8C+Eupa9/clP+0rD1Nn1pPlz4lQ+/3xRkKwUKpFpEonXLcw1NXbFu6cfOwexYbgUK3Y8vFlc9XGU8JzaOUKlM66eqBCZQJAGc5QB4YHJxKzZjpNc+eLI1oTaKZIiBIV+xyQQKEBqcEx2vR+AKEs5y3MQdAxhD2tFVqyOx1ecK1ge6UKH8iqRvlM4vVAr4F5/QIM6 ";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        PBMotor = hardwareMap.get(DcMotor.class, "PB");
        PFMotor = hardwareMap.get(DcMotor.class, "PF");
        DBMotor = hardwareMap.get(DcMotor.class, "DB");
        DFMotor = hardwareMap.get(DcMotor.class, "DF");
        lift  = hardwareMap.get(DcMotor.class, "Back");
        MServo  = hardwareMap.get(Servo.class, "MS");
        Cam = hardwareMap.get(WebcamName.class, "Webcam 1");

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

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            lift.setPower(-1);
            runtime.reset();

            while (runtime.milliseconds()<500) {

            }
            lift.setPower(0);

            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                      if (updatedRecognitions.size() == 2) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                          if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                          } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                          } else {
                            silverMineral2X = (int) recognition.getLeft();
                          }
                        }
                          if (goldMineralX != -1 && silverMineral1X != -1) {
                              if (goldMineralX > silverMineral1X) {
                                  telemetry.addData("Gold Mineral Position", "Middle");
                                  telemetry.update();
                                  //goldMiddle();
                              } else if (goldMineralX < silverMineral1X) {
                                  telemetry.addData("Gold Mineral Position", "Left");
                                  telemetry.update();
                                  //goldLeft();
                              }
                          } else {
                              telemetry.addData("Gold Mineral Position", "Right");
                              telemetry.update();
                              //goldRight();
                        }



                          /*if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {

                            }
                          }*/
                      }
                      telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = Cam;
        //parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    private void goldRight() {
//raise lift
        lift.setPower(-1);
        runtime.reset();

        while (runtime.milliseconds()<5500) {

        }
        lift.setPower(0);

//strafe right
        PBMotor.setPower(-.5);
        PFMotor.setPower(.5);
        DBMotor.setPower(-.5);
        DFMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<250) {

        }
//backup
        DFMotor.setPower(.5);
        PBMotor.setPower(-.5);
        PFMotor.setPower(-.5);
        DBMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<500) {

        }

        DFMotor.setPower(0);
        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);
//lower lift
        lift.setPower(1);
        runtime.reset();

        while (runtime.milliseconds()<6000) {

        }
        lift.setPower(0);



    }
    private void goldMiddle() {

        lift.setPower(-1);
        runtime.reset();

        while (runtime.milliseconds() < 5500) {

        }
        lift.setPower(0);


        DFMotor.setPower(.5);
        PBMotor.setPower(-.5);
        PFMotor.setPower(-.5);
        DBMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<300) {

        }

        DFMotor.setPower(-.5);
        PBMotor.setPower(.5);
        PFMotor.setPower(.5);
        DBMotor.setPower(-.5);

        runtime.reset();
        while (runtime.milliseconds()<300) {

        }

        DFMotor.setPower(.3);
        PBMotor.setPower(-.3);
        PFMotor.setPower(-.3);
        DBMotor.setPower(.3);

        runtime.reset();
        while (runtime.milliseconds()<100) {

        }
        DFMotor.setPower(-.25);
        PBMotor.setPower(-.25);
        PFMotor.setPower(-.25);
        DBMotor.setPower(-.25);

        runtime.reset();
        while (runtime.milliseconds()<1000) {

        }


        DFMotor.setPower(.5);
        PBMotor.setPower(-.5);
        PFMotor.setPower(-.5);
        DBMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<300) {

        }

        DFMotor.setPower(.3);
        PBMotor.setPower(-.3);
        PFMotor.setPower(-.3);
        DBMotor.setPower(.3);

        runtime.reset();
        while (runtime.milliseconds()<750) {

        }

        DFMotor.setPower(-.25);
        PBMotor.setPower(-.25);
        PFMotor.setPower(-.25);
        DBMotor.setPower(-.25);

        runtime.reset();
        while (runtime.milliseconds()<420) {

        }

        PBMotor.setPower(-.5);
        PFMotor.setPower(.3);
        DBMotor.setPower(-.3);
        DFMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<1000) {

        }

        PBMotor.setPower(-.5);
        PFMotor.setPower(-.4);
        DBMotor.setPower(.4);
        DFMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<1500) {

        }

        DFMotor.setPower(0);
        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);

        MServo.setPosition(Servo.MIN_POSITION);
        runtime.reset();

        while (runtime.milliseconds()<100) {

        }
        MServo.setPosition(Servo.MIN_POSITION);
        runtime.reset();

        while (runtime.milliseconds()<100) {

        }
        MServo.setPosition(Servo.MIN_POSITION);


        PBMotor.setPower(.35);
        PFMotor.setPower(.35);
        DBMotor.setPower(-.45);
        DFMotor.setPower(-.45);



        runtime.reset();
        while (runtime.milliseconds()<1950) {

        }

        runtime.reset();
        while (runtime.milliseconds()<350) {

        }

        DFMotor.setPower(0);
        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);


        telemetry.addData("Path", "Complete");
        telemetry.update();

        lift.setPower(1);


        runtime.reset();
//CHANGE THIS TO 3000 FOR ACTUAL GO TIME
        while (runtime.milliseconds()<6000) {

        }
        lift.setPower(0);

        PBMotor.setPower(.35);
        PFMotor.setPower(.35);
        DBMotor.setPower(-.35);
        DFMotor.setPower(-.35);

        runtime.reset();
        while (runtime.milliseconds()<2000) {

        }

        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);
        DFMotor.setPower(0);


    }

    private void goldLeft() {
//raise lift
        lift.setPower(-1);
        runtime.reset();

        while (runtime.milliseconds()<5500) {

        }
        lift.setPower(0);

        DFMotor.setPower(.3);
        PBMotor.setPower(-.3);
        PFMotor.setPower(-.3);
        DBMotor.setPower(.3);

        runtime.reset();
        while (runtime.milliseconds()<100) {

        }
        DFMotor.setPower(-.25);
        PBMotor.setPower(-.25);
        PFMotor.setPower(-.25);
        DBMotor.setPower(-.25);

        runtime.reset();
        while (runtime.milliseconds()<1000) {

        }


        DFMotor.setPower(.5);
        PBMotor.setPower(-.5);
        PFMotor.setPower(-.5);
        DBMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<300) {

        }

        PBMotor.setPower(-.5);
        PFMotor.setPower(.4);
        DBMotor.setPower(-.4);
        DFMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<850) {

        }

        DFMotor.setPower(-.25);
        PBMotor.setPower(-.25);
        PFMotor.setPower(-.25);
        DBMotor.setPower(-.25);

        runtime.reset();
        while (runtime.milliseconds()<420) {

        }

        PBMotor.setPower(-.5);
        PFMotor.setPower(.3);
        DBMotor.setPower(-.3);
        DFMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<700) {

        }

        PBMotor.setPower(-.5);
        PFMotor.setPower(-.4);
        DBMotor.setPower(.4);
        DFMotor.setPower(.5);

        runtime.reset();
        while (runtime.milliseconds()<1500) {

        }

        DFMotor.setPower(0);
        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);

        MServo.setPosition(Servo.MIN_POSITION);
        runtime.reset();

        while (runtime.milliseconds()<100) {

        }
        MServo.setPosition(Servo.MIN_POSITION);
        runtime.reset();

        while (runtime.milliseconds()<100) {

        }
        MServo.setPosition(Servo.MIN_POSITION);


        PBMotor.setPower(.35);
        PFMotor.setPower(.35);
        DBMotor.setPower(-.45);
        DFMotor.setPower(-.45);



        runtime.reset();
        while (runtime.milliseconds()<1950) {

        }

        runtime.reset();
        while (runtime.milliseconds()<350) {

        }

        DFMotor.setPower(0);
        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);


        telemetry.addData("Path", "Complete");
        telemetry.update();

        lift.setPower(1);


        runtime.reset();
//CHANGE THIS TO 3000 FOR ACTUAL GO TIME
        while (runtime.milliseconds()<6000) {

        }
        lift.setPower(0);

        PBMotor.setPower(.35);
        PFMotor.setPower(.35);
        DBMotor.setPower(-.35);
        DFMotor.setPower(-.35);

        runtime.reset();
        while (runtime.milliseconds()<2000) {

        }

        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);
        DFMotor.setPower(0);
    }
}
