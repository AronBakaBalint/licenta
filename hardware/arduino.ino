#include <LiquidCrystal.h>
//LCD pin to Arduino
const int pin_RS = 8; 
const int pin_EN = 9; 
const int pin_d4 = 4; 
const int pin_d5 = 5; 
const int pin_d6 = 6; 
const int pin_d7 = 7; 
const int pin_BL = 10; 
LiquidCrystal lcd( pin_RS,  pin_EN,  pin_d4,  pin_d5,  pin_d6,  pin_d7);
String message; // for incoming serial data
void setup() {
 pinMode(49, OUTPUT);
 pinMode(53, OUTPUT);
 lcd.begin(16, 2);
 lcd.setCursor(0,0);
 Serial.begin(9600);
}
void loop() {
 if (Serial.available() > 0) {
    message = Serial.readString();
    message.trim();
    handleMessage(message);
  }
  redLight();
} 

void handleMessage(String message){
  if(message.equals("welcome")){
      openBarrier();
    } else if(message.equals("no money")){
      displayNoMoney();  
    } else if(message.equals("reservation expired")){
      displayReservationExpired();
    } else if(message.equals("no reservation found")){
      displayNoReservation();
    } else {
      handleDeparture(message);
    }
}

void handleDeparture(String message){
  String price = getValue(message, ';', 0);
      String balance = getValue(message, ';', 1);
      lcd.clear();
      lcd.print("-"+price);
      lcd.setCursor(0,1);
      lcd.print("sold:"+balance+" lei");
      delay(4000);
      lcd.clear();
      openBarrier();
}

String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}

void displayNoReservation(){
  lcd.clear();
  lcd.print("You have");
  lcd.setCursor(0,1);
  lcd.print("no reservations");
  delay(4000);
  lcd.clear();
}

void displayReservationExpired(){
  lcd.clear();
  lcd.print("Reservation");
  lcd.setCursor(0,1);
  lcd.print("expired");
  delay(4000);
  lcd.clear();
}

void displayNoMoney(){
  lcd.print("Not enough money");
}

void openBarrier(){
  greenLight();
  delay(8000);
  redLight();
}

void greenLight(){
  digitalWrite(49, HIGH);
  digitalWrite(53, LOW);
}

void redLight(){
  digitalWrite(49, LOW);
  digitalWrite(53, HIGH);
}
