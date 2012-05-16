<?php

//Instructions and code from http://www.web-development-blog.com/archives/send-e-mail-messages-via-smtp-with-phpmailer-and-gmail/

require_once('phpmailer/class.phpmailer.php');

define('GUSER', 'opencomm.cs@gmail.com'); // GMail username
define('GPWD', '@pencomm'); // GMail password

//taken on http://wiki.jumba.com.au/wiki/PHP_Generate_random_password
function createPassword($length) {
	$chars = "234567890abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	$i = 0;
	$password = "";
	while ($i <= $length) {
		$password .= $chars{mt_rand(0,strlen($chars))};
		$i++;
	}
	return $password;
}

function encrypt($str, $key)
{
    $block = mcrypt_get_block_size('des', 'ecb');
    $pad = $block - (strlen($str) % $block);
    $str .= str_repeat(chr($pad), $pad);

    return mcrypt_encrypt(MCRYPT_DES, $key, $str, MCRYPT_MODE_ECB);
}

function smtpmailer($to, $from, $from_name, $subject, $body,$altbody) { 
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
	$mail->isHTML(true);
	$mail->AltBody = $altbody;
	$mail->AddAddress($to);	
	if(!$mail->Send()) {
		//Problem with the mail
		return false;
	} else {
		return true;
	}
} 

?>

<?php
		
//Those three variables have to be stored on external files
$serverOpenfire = 'http://cuopencomm.no-ip.org';
$key = "opencomm.123";
$witness = "opencomm";
//the witness is used to verify that the right encryption algorithm is used (so that we don't get pirated).

if(isset($_POST['userEmail']) && isset($_POST['action'])) {
	$userEmail = $_POST['userEmail'];
	$action = $_POST['action'];
 	$usernameArray = preg_split('/@/',$userEmail);
	$username = $usernameArray[0].$usernameArray[1]; 
	if($action === "forgot") {
		$password = createPassword(8);
		$subject = 'Your new password for OpenComm';
		//in HTML format
		$body = 'Your password is now: '.$password;
		//in text format (if the HTML does not work)
		$altbody = 'Your password is now: '.$password;
		$suffix = ':9090/plugins/userService/userservice?type=update&secret=opencomm.123&username='.$username.'&password='.$password;
	}
	else if($action === "add") {
		if(isset($_POST['id']) && isset($_POST['password'])) {
			$id = preg_replace('/ +/', '%20', $_POST['id']);
			$password = $_POST['password'];
			
			//encryption
			/*$id = encrypt($id, $key);
			$username = encrypt($username, $key);
			$password = encrypt($password, $key);
			$userEmailEnc = encrypt($userEmail, $key);
			
			$witnessEnc = encrypt($witnessEnc, $key); */
			$userEmailEnc = $userEmail;
			$witnessEnc = $witness;
			
			$url = 'http://199.167.198.149/confirmation.php?last='.$witnessEnc.'&username='.$username.'&password='.$password.'&name='.$id.'&email='.$userEmailEnc;
			
			//mail
			$subject = 'OpenComm registration';
			//html body
			$body = '<html>
					<body>
					<p>You have nearly signed up to Opencomm, please click on this 
					<a href = "'.$url.'">link</a> to confirm.
					</p>
					</body>
					</html>';
			//text body
			$altbody = 'You have nearly signed up to Opencomm, please click on the following link to confirm: '.$url;
		}
		else {
			echo "PB1";
			break;	
		}
	}
	else if($action === "edit") {
		if(isset($_POST['id']) && isset($_POST['password'])) {
			$id = preg_replace('/ +/', '%20', $_POST['id']);
			$password = $_POST['password'];
			$suffix =':9090/plugins/userService/userservice?type=update&secret=opencomm.123&username='.$username.'&password='.$password.'&name='.$id.'&email='.$userEmail;
		}
		else {
			echo "PB1";
			break;	
		}
	}
	else {
		echo "PB4";
		break;	
	}

	if($action === "add" || $action === "forgot") { 
		if(smtpmailer($userEmail, 'opencomm.cs@gmail.com', 'OpenComm Consumer Service', $subject, $body,$altbody)) {	
			if($action === "forgot") {
				$fp = fopen($serverOpenfire.$suffix, 'rb');
				
				if (!$fp) {
					echo "PB2";
				  }
				else {
					$response = @stream_get_contents($fp);
					if ($response === false) {
						echo "PB3";
						}
					else {
						$response = preg_replace('/<[^>]*>|[^a-zA-Z]/', '', $response);
						echo $response;
						}
					}
				}
			else if($action === "add") {
				echo "ok";
				}
			else {
				echo "Wrong action";
				}
			}
		else {
			echo "PB5";	
			}
		}
	else {
		$fp = fopen($serverOpenfire.$suffix, 'rb');	
		if (!$fp) {
			echo "PB2";
		  }
		else {
			$response = @stream_get_contents($fp);
			if ($response === false) {
				echo "PB3";
				}
			else {
				$response = preg_replace('/<[^>]*>|[^a-zA-Z]/', '', $response);
				echo $response;
				}
			}
		}
	}
else {
	echo "PB1";	
	}
?>
