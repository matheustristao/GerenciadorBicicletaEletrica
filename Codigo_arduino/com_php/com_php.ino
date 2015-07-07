#define sol 3 //Define Lamp como 3
#define energia 7 //Define energia pin como 7
#define FUNC6_SOLICITACAO_LIBERAR  1
#define FUNC6_SOLICITACAO_CORTAR  2
#define FUNC6_SOLICITACAO_FALSE 0
#define FALHA_FUNC7_LIBERAR -1
#define FALHA_FUNC7_CORTAR -2

#include <SPI.h>
#include <Ethernet.h>
#include <EmonLib.h>  

// domínio do servidor
char serverName[] = "192.168.1.166";

// porta do servidor
int serverPort = 80;

// Pagina web a ser acessada do servidor
char pageName[] = "/xampp/server_bike/server.php";

EthernetClient client;
int totalCount = 0;
// insure params is big enough to hold your variables
char params[32];

// delay de carregamento
#define delayMillis 2000UL

unsigned long thisMillis = 0;
unsigned long lastMillis = 0;

byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};

//var bateria
EnergyMonitor emon1;
 
//Tensao da rede eletrica
int rede = 220.0; 
int pino_sct =A0; //Pino do sensor SCT


// var Solenoide
int solenoide_current = 0;
int solenoide_last = 0;
int estado_energia = 0;


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

  setupSolenoide();
  setupEnergia();
  setupDadosBateria();

}

void loop()
{
  char * out_msg;
  // If using a static IP, comment out the next line
  Ethernet.maintain();

  thisMillis = millis();

  if (thisMillis - lastMillis > delayMillis)
  {
    lastMillis = thisMillis;

    // params must be url encoded.
    sprintf(params, "numerofuncao=6");
    int flag = postPage(serverName, serverPort, pageName, params);

    if (flag == FUNC6_SOLICITACAO_FALSE) // nao roda funcao 7
      Serial.println(F("Nao ha solicitacao "));
    else {

      if (flag == FUNC6_SOLICITACAO_LIBERAR) {// Liberar energia
        sprintf(params, "numerofuncao=7&tranca=1&flag=0");
        acionamentosLOW();

      }

      if (flag == FUNC6_SOLICITACAO_CORTAR) { // Fechar energia
        sprintf(params, "numerofuncao=7&tranca=2&flag=0");
        acionamentosHIGH();


      }
      flag = postPage(serverName, serverPort, pageName, params); // POST no servidor

      /*
        // tratando retorno
        if (flag == FALHA_FUNC7_CORTAR)
          sprintf(out_msg, "Falha ao cortar %s", params);
        else if (flag == FALHA_FUNC7_LIBERAR)
          sprintf(out_msg, "Falha ao liberar %s", params);

        sprintf(out_msg, "Finalizado funcao 7 %s", params);
      */
      controleTomada();

      Serial.println("\n------Dados ------\n");

      Serial.println("\n------Solicitacao Processada ------\n");
    }

    Serial.println(out_msg);

    totalCount++;
    Serial.println();
    Serial.println(totalCount, DEC);
    Serial.println("Disconnecting");

    // envia o valor da bateria para o servidor
    mostraValorBateria();

    // seta ultimo valor do solenoide
    setSolLast();
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
      Serial.print(inChar);
      if (inChar == '@') // caracter do retorno echo php
      {
        startRead = true;
      }
      else if (startRead == true)
      {
        client.stop();
        if (inChar == '1') {
          return 1;
        } else         if (inChar == '2') {
          return 2;
        } else  if (inChar == '-') {
          int retorno = inChar * (-1);
          return retorno;
        } if (inChar == '0') {
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

void acionamentosLOW() {

  digitalWrite(sol, LOW);

}


void acionamentosHIGH() {

  digitalWrite(sol, HIGH);

}

void setupSolenoide() {

  pinMode(sol, OUTPUT); //Define o pino solenoide como saída
  // pinMode(sensor, INPUT);
  // digitalWrite(sensor, HIGH);
  Serial.println("rodei setup");
}

void setupEnergia() {

  pinMode(energia, OUTPUT); //Define o pino energia como saída
  // pinMode(sensor, INPUT);
  // digitalWrite(sensor, HIGH);
  Serial.println("rodei setup energia");
  acionamentoEnergiaLOW();


}



void controleTomada() {

  setSolLast();

  if (solenoide_current > solenoide_last) {
    if (estado_energia == 0)
    {
      estado_energia = 1;
    } else     if (estado_energia == 1)
    {
      estado_energia = 0;
    }

  }

  if (estado_energia == 0 )
  {
    acionamentoEnergiaLOW();
    Serial.println("acionamentoEnergiaHIGH");
  } else {
    acionamentoEnergiaHIGH();
    Serial.println("acionamentoEnergiaLOW");
  }

  solenoide_current = solenoide_last;
}

void setSolLast() {
  solenoide_last = digitalRead(sol);
}

void setSolCurr() {
  solenoide_current = digitalRead(sol);
}

void acionamentoEnergiaLOW() {
  digitalWrite(energia, LOW);
}

void acionamentoEnergiaHIGH() {
  digitalWrite(energia, HIGH);
}



void mostraValorBateria() 
{ 

  double Irms = emon1.calcIrms(1480);

  client.println(Irms,3);

  // Serial.print("Corrente : ");  //Mostra o valor da corrente
  Serial.print(Irms,3); // Irms
  // Serial.print(" Tensao : ");
  Serial.print(rede);     
  // Serial.print(" Potencia : ");
  Serial.println(Irms*rede);
  
}  
  
void setupDadosBateria(){
  //Pino, calibracao - Cur Const= Ratio/BurdenR. 1800/62 = 29. 
  emon1.current(pino_sct, 40.1);//111.1 para 110 volts
}

