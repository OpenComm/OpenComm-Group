<?php 

//must be stored on a separate secure file. You could also check out openssl_public_encrypt/decrypt for the key.
$serverOpenfire = 'http://cuopencomm.no-ip.org';
$key = "opencomm.123";
$witness = "opencomm";

function decrypt($str, $key)
{   
    $str = mcrypt_decrypt(MCRYPT_DES, $key, $str, MCRYPT_MODE_ECB);

    $block = mcrypt_get_block_size('des', 'ecb');
    $pad = ord($str[($len = strlen($str)) - 1]);
    return substr($str, 0, strlen($str) - $pad);
}

$string = "Unknown error";

if(isset($_GET['username'],$_GET['name'], $_GET['password'], $_GET['email'], $_GET['last'])) {
	/*if($witness === decrypt($_GET['last'], $key)) {	
		$username = decrypt($_GET['username'], $key);
		$password = decrypt($_GET['password'], $key);
		$id = decrypt($_GET['name'], $key);
		$userEmail = decrypt($_GET['email'], $key);*/
	if($witness === $_GET['last']) {	
		$username = $_GET['username'];
		$password = $_GET['password'];
		$id = $_GET['name'];
		$userEmail = $_GET['email'];
		
		$suffix =':9090/plugins/userService/userservice?type=add&secret=opencomm.123&username='.$username.'&password='.$password.'&name='.$id.'&email='.$userEmail;
		$fp = fopen($serverOpenfire.$suffix, 'rb');
		if (!$fp) {
			$string = "Can't access the server, try again later.";
		  }
		else {
			$response = @stream_get_contents($fp);
			if ($response === false) {
				$string = "Invalid response from the server, try again later.";
				}
			else {
				$response = preg_replace('/<[^>]*>|[^a-zA-Z]/', '', $response);
				if($response ==="ok") {
					$string = "You have successfully signed up to OpenComm!";
					}
				}
			}
		}
	else {
		$string = "Incorrect data.";
		echo $witness;
		echo decrypt($_GET['username'], $key);
		echo $_GET['email'];
	}
}
else {
	$string = "The link you accessed is incorrect.";		
}
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OpenComm confirmation page</title>
</head>

<body>
<p>
<?php
echo $string;
?>
</p>
</body>
</html>
