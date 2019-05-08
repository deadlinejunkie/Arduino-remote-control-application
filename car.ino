
/*
 * Arduino Car, Bluetooth remote control
 * Copyright 2019 Anastasiya Sukhotskaya
 * Used libraries: "AFMotor.h" <https://github.com/adafruit/Adafruit-Motor-Shield-library/blob/master/AFMotor.h>
 */

#include <AFMotor.h>

// define motors
AF_DCMotor motorFront();  //  front motor
AF_DCMotor motorRear();   //  rear motor

const byte SPEED_MIN = 100; // minimal motor speed not to stop
const byte SPEED_1 = 125;
const byte SPEED_2 = 150;
const byte SPEED_3 = 175;
const byte SPEED_4 = 200;
const byte SPEED_5 = 225;
const byte SPEED_MAX = 255; // maximum motor speed
byte SPEED_CURRENT = 0; // current momtos speed

const char DIRECTION1 = FORWARD; 
const char DIRECTION2 = BACKWARD; 

// turn direction
const byte RIGHT   = 1;  // right turn
const byte LEFT    = 3;  // left turn

/* pins for module coonection
 * 13, 2 are digital pins
 * 14, 15 are analog pins A0 and A1
 */
 
#define PIN_TRIG 14 //13
#define PIN_ECHO 15 //2

// for bluetooth control
char btAdapter = 'S';

// Counters for bluetooth loose connection
unsigned long btTimer0 = 2000;  // stores the time (in millis since execution started)
unsigned long btTimer1 = 0;     // stores the time when the last command was received from the phone

/******************************************
  Main program
******************************************/

void setup() {
  Serial.begin(9600);
  pinMode(PIN_TRIG, OUTPUT);
  pinMode(PIN_ECHO, INPUT);
  motorUp();
}

void loop() {
  if (Serial.available() > 0) {
    btTimer1 = millis();
    btCommand = Serial.read(); 
   
    switch (btCommand){
    case 'btnUp':
      motorUp(); // turn the motor on
      break;
    case 'btnStop':
      motorStop(); // turn the motor off
      break;
    case 'btnClutch':
      motorClutch(); // clutch the motor and transmission
      break;
    case 'btnLeft':
      motorRotateLeft(); // turn left
      break;
    case 'btnRight':
      motorRotateRight(); // turn right
      break;
    case 'btnSpeedOne':  
      motorSpeedOne(); // choose speed one (forward)
      break;
    case 'btnSpeedTwo':
      motorSpeedTwo(); // choose speed two (forward)
      break;
    case 'btnSpeedThree': // choose speed three (forward)
      motorSpeedThree();
      break;
    case 'btnSpeedFour':
      motorSpeedFour(); // choose speed four (forward)
      break;
    case 'btnSpeedFive': 
      motorSpeedFive(); // choose speed five (forward)
      break;
    }
  else{
    btTimer0 = millis();  // get the current time (millis since execution started).
    // check if it has been 500ms since we received last btCommand.
    if ((btTimer0 - btTimer1) > 500)   {
      // more tan 500ms have passed since last btCommand received, car is out of range.
      // therefore stop the car
      motorStop();
    }
  }
}
 
/******************************************
  Functions
******************************************/

// motor initialization
void motorUp()  {
  // turn on motor
  motorSetSpeed(SPEED_MIN); // set SPEED_MIN = 100 value to motor
  // motorStop();
}
 
 void motorStop()  {
  // turn off the motor
  motorSetSpeed(0); 
}
 
boolean motorClutch(){
 //clutch the motor and transmission
  return true;
}
 
void motorRotateLeft(){
 // turn left
  motorFront.run(LEFT);
  motorRear.run(LEFT);
}
 
void motorRotateRight(){
 // turn right
  motorFront.run(RIGHT);
  motorRear.run(RIGHT);
}

// all the functions below set speed and direction
 
void motorSetSpeed(int speed)  {
  motorFront.setSpeed(speed);
  motorRear.setSpeed(speed);
  SPEED_CURRENT = speed;
}
 
void motorSetDirection(char direction){
  motorFront.setDirection(direction);
  motorRear.setDirection(direction);
}
 
void motorSpeedOne(int speed, char direction){
 motorSetSpeed(SPEED_1);
 motorSetDirection(direction);
}
 
 void motorSpeedTwo(int speed, char direction){
 motorSetSpeed(SPEED_2);
 motorSetDirection(direction);
}
 
void motorSpeedThree(int speed, char direction){
 motorSetSpeed(SPEED_3);
 motorSetDirection(direction);
}
 
void motorSpeedFour(int speed, char direction){
 motorSetSpeed(SPEED_4);
 motorSetDirection(direction);
}
 
void motorSpeedFive(int speed, char direction){
 motorSetSpeed(SPEED_5);
 motorSetDirection(direction);
}
