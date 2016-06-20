/*
 * Instruction Protocol
 * 
 * Receipt
 *    Handshake : 'H'
 *    Move : 'Sddd#' / 'Tdd#dd#ddd' (d -> digit, # -> +/-)
 *    Quit : 'Q'
 * 
 * Send
 *    Handshake : 'HELLO READY'
 *    Receipt of move instruction : 'GOT IT'
 *    Completion of move instruction : 'DONE'
 *    Faliure of move instruction : 'FAIL'
 *    Quit : 'BYE'
 */

#include <SPI.h>
#include <WiFi.h>

char ssid[] = "Home";      
char pass[] = "0777002310";  
int keyIndex = 0;     // needed only for WEP

int status = WL_IDLE_STATUS;

WiFiServer server(12346);
WiFiClient client;

void setup()
{
  pinMode(A0, OUTPUT); 
  pinMode(A1, OUTPUT); 
  pinMode(A2, OUTPUT); 
  pinMode(A3, OUTPUT); 
  pinMode(A4, INPUT); 
  pinMode(A5, INPUT);
  digitalWrite(A0, LOW);
  digitalWrite(A1, LOW);
  digitalWrite(A2, LOW);
  digitalWrite(A3, LOW);
  //Initialize serial and wait for port to open
  Serial.begin(9600); 
  
  while (!Serial)
  {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD)
  {
    Serial.println("WiFi shield not present");   
    while(true);    // don't continue
  } 
  
  // attempt to connect to Wifi network:
  while ( status != WL_CONNECTED)
  { 
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    
    // Connect to WPA/WPA2 network   
    status = WiFi.begin(ssid, pass);

    /*
    // Connect to Open network
    status = WiFi.begin(ssid);
    */
    
    // wait 10 seconds for connection:
    delay(10000);
  } 
  server.begin();
  /*
  // Debug : Print the status
  printWifiStatus();
  */
}

void loop()
{
  // listen for incoming clients on port 12346
  client = server.available();
  if (client)
  {
    /*
    // Debug
    Serial.println("Client connected.");
    */
    
    while (client.connected())
    {
      if (client.available())
      {
        char c = getValidChar();
        char left;
        char right;
        int count;
        int i;
        /*
        Serial.write(c);
        */
        
        if (c == 'H')
        {
           client.println("HELLO READY");
           while (c != 'Q')
           {
            c = getValidChar();
            if (c == 'S')
            {
              count = 0;
              i = 0;
              while(i<3)
              {
                c = getValidChar();
                count = count * 10 + (c - 48);
                i++;
              }
              c = getValidChar();
              if (c == '-' || c == '+')
              {
              client.println("GOT IT");
                if (c == '-')
                {
                  robotMove(-1, -1, count);            
                }
                else
                {
                  robotMove(1, 1, count);
                }
              client.println("DONE");
              }
            }
            else if (c == 'T')
            {
              left = 0;
              right = 0;
              count = 0;
              i = 0;
              while(i<2)
              {
                c = getValidChar();
                left = left * 10 + (c - 48);
                i++;
              }
              c = getValidChar();
              if (c == '-')
                left = left * (-1);
              while(i<4)
              {
                c = getValidChar();
                right = right * 10 + (c - 48);
                i++;
              }
              c = getValidChar();
              if (c == '-')
              {
                right = right * (-1);
              }
              while(i<7)
              {
                c = getValidChar();
                count = count * 10 + (c - 48);
                i++;
              }
              client.println("GOT IT");
              robotMove(left, right, count);
              client.println("DONE");
            }
           }
          client.println("BYE");
          client.stop();    // close the connection
        }
      }
    }
  }
}

char getValidChar()
{
  char c = client.read();
  while(c == (-1))
  {
    c = client.read();
  }
  return c;
}
void robotMove(char left, char right, int count)
{
  while(count > 0)
  {
    char val;
    digitalWrite(A0, HIGH);
    if(left > 0)
    {
      digitalWrite(A2, HIGH);
      val = digitalRead(A4);
      waitSegl(left, val);
      digitalWrite(A2, LOW);
    }
    else
    {
      digitalWrite(A3, HIGH);
      val = digitalRead(A4);
      waitSegl(-left, val);
      digitalWrite(A3, LOW);
    }
    digitalWrite(A0, LOW);
    digitalWrite(A1, HIGH);
    if(right > 0)
    {
      digitalWrite(A2, HIGH);
      val = digitalRead(A5);
      waitSegr(right, val);
      digitalWrite(A2, LOW);
    }
    else
    {
      digitalWrite(A3, HIGH);
      val = digitalRead(A5);
      waitSegr(-right, val);
      digitalWrite(A3, LOW);
    }
    digitalWrite(A1, LOW);
    count--;
  }
}

void waitSegl(char segments, char val)
{
  while(segments > 0)
  {
    delay(150);    
    segments--;
  }
  /*
  while(segments > 0)
  {
    while(digitalRead(A4) != val)
    {
      ;
    }
    while(digitalRead(A4) == val)
    {
      ;
    }
    segments--;
  }
  */
}

void waitSegr(char segments, char val)
{
  while(segments > 0)
  {
    delay(240);    
    segments--;
  }
  /*
  while(segments > 0)
  {
    while(digitalRead(A5) != val)
    {
      ;
    }
    while(digitalRead(A5) == val)
    {
      ;
    }
    segments--;
  }
  */
}
/*
  // Debug : Print the status
void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}
*/
