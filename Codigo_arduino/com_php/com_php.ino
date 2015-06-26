/*
   Web client sketch for IDE v1.0.1 and w5100/w5200
   Uses POST method.
   Posted November 2012 by SurferTim
*/

#include <SPI.h>
#include <Ethernet.h>

byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};

//Change to your server domain
char serverName[] = "192.168.0.10";

// change to your server's port
int serverPort = 80;

// change to the page on that server
char pageName[] = "/xampp/server_bike_php/server.php";

EthernetClient client;
int totalCount = 0;
// insure params is big enough to hold your variables
char params[32];

// set this to the number of milliseconds delay
// this is 30 seconds
#define delayMillis 5000UL

unsigned long thisMillis = 0;
unsigned long lastMillis = 0;

void setup() {
  Serial.begin(9600);

  // disable SD SPI
  pinMode(4, OUTPUT);
  digitalWrite(4, HIGH);

  Serial.print(F("Starting ethernet..."));
  if (!Ethernet.begin(mac)) Serial.println(F("failed"));
  else Serial.println(Ethernet.localIP());

  delay(2000);
  Serial.println(F("Ready"));
}

void loop()
{
  // If using a static IP, comment out the next line
  Ethernet.maintain();

  thisMillis = millis();

  if (thisMillis - lastMillis > delayMillis)
  {
    lastMillis = thisMillis;

    // params must be url encoded.
    sprintf(params, "numerofuncao=6");
    int flag = postPage(serverName, serverPort, pageName, params);

    if (flag < 0)
      Serial.print(F("Fail "));
    else if (flag == 1)
    {
      sprintf(params, "numerofuncao=7&tranca=1&flag=0");
      flag = postPage(serverName, serverPort, pageName, params);

      if (!flag)
        Serial.print(F("Fail numerofuncao=7 "));
      else
        Serial.print(F("OK numerofuncao=7 "));
    }
    totalCount++;
    Serial.println();
    Serial.println(totalCount, DEC);
    Serial.println("Disconnecting");
  }
}


byte postPage(char* domainBuffer, int thisPort, char* page, char* thisData)
{
  char inChar;
  char outBuf[64];

  Serial.print(F("connecting..."));

  if (client.connect(domainBuffer, thisPort) == 1)
  {
    Serial.println(F("connected"));

    // send the header
    sprintf(outBuf, "POST %s HTTP/1.1", page);
    client.println(outBuf);
    sprintf(outBuf, "Host: %s", domainBuffer);
    client.println(outBuf);
    client.println(F("Connection: close\r\nContent-Type: application/x-www-form-urlencoded"));
    sprintf(outBuf, "Content-Length: %u\r\n", strlen(thisData));
    client.println(outBuf);

    // send the body (variables)
    client.print(thisData);

  }
  else
  {
    Serial.println(F("failed"));
    return 0;
  }

  int connectLoop = 0;
  boolean startRead = false; // is reading?

  while (client.connected())
  {

    while (client.available())
    {
      inChar = client.read();
      if (inChar == '@') // caracter do retorno echo php
      {
        startRead = true;
      }
      else if (startRead == true)
      {
        Serial.write(inChar);
        client.stop();
        if (inChar == '1') {
          Serial.println("Retorno 1");
          Serial.println();
          return 1;
        } else {
          sprintf(thisData, "Retorno alternativo %c", inChar);
          Serial.println(thisData);
          Serial.println();
          return 0;
        }
      }
      connectLoop = 0;
    }
    delay(1);
    connectLoop++;
    if (connectLoop > 10000)
    {
      Serial.println();
      Serial.println(F("Timeout"));
      client.stop();
    }
  }
  return -1;
}
