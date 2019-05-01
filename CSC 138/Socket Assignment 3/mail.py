from socket import *

#def main():
msg='\r\n I am sending myself an email to test my mail server'
endmsg='\r\n.\r\n'
mailServer ='smtp.csus.edu'
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((mailServer, 25))
recv=clientSocket.recv(1024)
print recv
if recv[:3]!='220':
	print '220 reply not received from server.'

#Send HELO command and print server response.
heloCommand='HELO Localhost\r\n'
clientSocket.send(heloCommand)
recv1=clientSocket.recv(1024)
print recv1
if recv1[:3]!='250':
	print '250 reply not received from server.'

#Send MAIL FROM command and print server response.
clientSocket.send('MAIL FROM: <breeanageorge@csus.edu>\r\n')
recv1 = clientSocket.recv(1024)
print recv1
if recv1[:3] != '250': #if the data is not received
	print '250 reply not received from server.'

#Send RCPT TO command and print server response.
clientSocket.send('RCPT TO: <breegeo1@gmail.com> \r\n')
recv1 = clientSocket.recv(1024)
print recv1
if recv1[:3] != '250':
	print '250 reply not received from server.'

#Send DATA command and print server response.
clientSocket.send('DATA\r\n')
recv1 = clientSocket.recv(1024)
print recv1
if recv1[:3] != '354':
	print '250 reply not received from server.'

#Send message data.
clientSocket.send(msg)

#Message ends with a single period.
clientSocket.send(endmsg)
recv1 = clientSocket.recv(1024)
print recv1
if recv1[:3] != '250':
	print '250 reply not received from server.'

#Send QUIT command and get server response.
clientSocket.send('QUIT\r\n')
clientSocket.close()

#pass

#if __name__ == '__main__':
#main()