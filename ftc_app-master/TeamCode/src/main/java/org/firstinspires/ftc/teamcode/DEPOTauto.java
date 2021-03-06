/* Copyright (c) 2017 FIRST. All rights reserved.
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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="DEPOTauto", group="Auto")

public class DEPOTauto extends LinearOpMode {
    
    /* Declare OpMode members. */
    Hardware         robot   = new Hardware();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();
    private DcMotor lift;
    private DcMotor PBMotor;
    private DcMotor PFMotor;
    private DcMotor DBMotor;
    private DcMotor DFMotor;
    private Servo MServo;
    @Override
    public void runOpMode() {
            
        PBMotor = hardwareMap.get(DcMotor.class, "PB");
        PFMotor = hardwareMap.get(DcMotor.class, "PF");
        DBMotor = hardwareMap.get(DcMotor.class, "DB");
        DFMotor = hardwareMap.get(DcMotor.class, "DF");
        lift  = hardwareMap.get(DcMotor.class, "Back");
        MServo  = hardwareMap.get(Servo.class, "MS");
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
    
        
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :",
                          lift.getCurrentPosition());
        telemetry.update();

        MServo.setPosition(Servo.MAX_POSITION);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        lift.setPower(-1);
        runtime.reset();
        
        while (runtime.milliseconds()<6000) {
         
        }
        lift.setPower(0);

        runtime.reset();
        //change this number to add a delay
        while (runtime.milliseconds()<5) {

        }

        DFMotor.setPower(.5);
        PBMotor.setPower(-.5);
        PFMotor.setPower(-.5);
        DBMotor.setPower(.5);
        
        runtime.reset();
        while (runtime.milliseconds()<1100) {
         
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


        DFMotor.setPower(.25);
        PBMotor.setPower(.25);
        PFMotor.setPower(.25);
        DBMotor.setPower(.25);

        runtime.reset();
        while (runtime.milliseconds()<370) {

        }

        PBMotor.setPower(.4);
        PFMotor.setPower(-.25);
        DBMotor.setPower(.25);
        DFMotor.setPower(-.4);

        runtime.reset();
        while (runtime.milliseconds()<550) {

        }


        PBMotor.setPower(.4);
        PFMotor.setPower(.4);
        DBMotor.setPower(-.38);
        DFMotor.setPower(-.38);



        runtime.reset();
        while (runtime.milliseconds()<1400) {

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
        while (runtime.milliseconds()<2500) {

        }

        PBMotor.setPower(0);
        PFMotor.setPower(0);
        DBMotor.setPower(0);
        DFMotor.setPower(0);

    }
    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
   
}
