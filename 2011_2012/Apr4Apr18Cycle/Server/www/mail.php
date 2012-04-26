<?php

//Instructions and code from http://www.web-development-blog.com/archives/send-e-mail-messages-via-smtp-with-phpmailer-and-gmail/

require_once('PHPMailer/class.phpmailer.php');

define('GUSER', 'opencomm.cs@gmail.com'); // GMail username
define('GPWD', '@pencomm'); // GMail password

function smtpmailer($to, $from, $from_name, $subject, $body) { 
	global $error;
	$mail = new PHPMailer();  // create a new object
	$mail->IsSMTP(); // enable SMTP
	$mail->SMTPDebug = 0;  // debugging: 1 = errors and messages, 2 = messages only
	$mail->SMTPAuth = true;  // authentication enabled
	$mail->SMTPSecure = 'ssl'; // secure transfer enabled REQUIRED for GMail
	$mail->Host = 'smtp.gmail.com';
	$mail->Port = 465; 
	$mail->Username = GUSER;  
	$mail->Password = GPWD;           
	$mail->SetFrom($from, $from_name);
	$mail->Subject = $subject;
	$mail->Body = $body;
	$mail->AddAddress($to);
	if(!$mail->Send()) {
		//Problem with the mail
		$error = 'PB1'; 
		return false;
	} else {
		$error = 'OK';
		return true;
	}
} 

?>

<?php
$userEmail = $_POST['userEmail'];
if(isset($userEmail)) {
	smtpmailer($userEmail, 'opencomm.cs@gmail.com', 'OpenComm Consumer Service', 'Your new fucking password, bitch!','And don\'t fucking forget it again, dude!');
echo $error;
	}
else echo 'PB2';

?>
