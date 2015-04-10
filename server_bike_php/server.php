<?php

	
	$nome = $_POST['nome'];
	$sobrenome = $_POST['sobrenome'];
	$email = $_POST['email'];
	

	echo 'Variavel extraidas do server PHP e nao do android: Nome: ' .$nome. '  !!!'

	//Classe responsável pela conexão com o banco
	class ConectionFactory {

    static protected $mysqli;
    
    private function conectaBanco() {
        $MySQL = array(
            'servidor' => 'localhost',
            'usuario' => 'root',
            'senha' => 'root',
            'banco' => 'server_bike'
        );

        $mysqli = new mysqli($MySQL['servidor'], $MySQL['usuario'], $MySQL['senha'], $MySQL['banco']);

        if (mysqli_connect_errno()) {
            trigger_error(mysqli_connect_error(), E_USER_ERROR);
        } else {
            return $mysqli;
        }
    }

    public function executaQuery($query) {
        $instanciaMySqli = self::conectaBanco();

        $instanciaMySqli->query($query);
    }

    public function listData($query) {
        $instanciaMySqli = self::conectaBanco();

        return $result = $instanciaMySqli->query($query);
    }

	}

	//Classe responsável pelo cadastro de usuários no banco
	class UsuarioDao {

    static private $conection;

    private static function instanciaConection() {

        if (!isset(self::$conection)) {
            self::$conection = new ConectionFactory();
        }

        return self::$conection;
    }

    public function listUsuario($nome,$sobrenome) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
            P.NOME,
            P.SOBRENOME,
            P.EMAIL
                from  usuario P
                where 
                	P.NOME like '$nome'
                	and
                	P.SOBRENOME like '$sobrenome' 
                ";

        return $lista = $instanciaConection->listData($query);
    }

    public function saveUsuario($nome, $sobrenome, $email) {

        $instanciaConection = self::instanciaConection();

        $query1 = "insert into usuario (
                     NOME
                    ,SOBRENOME
                    ,EMAIL
                    ) VALUES ( 
                     '$nome' -- NOME - IN varchar(40)
                    ,'$sobrenome' -- SOBRENOME - IN varchar(100)
                    ,'$email' -- EMAIL - IN varchar(100)
                    ) ";

        $instanciaConection->executaQuery($query1);
            
    }


}



?>
