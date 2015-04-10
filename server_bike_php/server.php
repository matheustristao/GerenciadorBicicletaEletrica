<?php

    
    $nome = $_POST['nome'];
    $sobrenome = $_POST['sobrenome'];
    $email = $_POST['email'];
    $telefone = $_POST['telefone'];
    $senha_usuario = $_POST['senha'];
    

    //echo 'Variavel extraidas do server PHP e nao do android: Nome: ' .$nome. '  !!!';

    $userDao = new UsuarioDao();
    
    $userDao->saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario);

    //Classe responsável pela conexão com o banco
    class ConectionFactory {

    static protected $mysqli;
    
    private function conectaBanco() {
        $MySQL = array(
            'servidor' => 'localhost',
            'usuario' => 'root',
            'senha_banco' => 'root',
            'banco' => 'server_bike'
        );

        $mysqli = new mysqli($MySQL['servidor'], $MySQL['usuario'], $MySQL['senha_banco'], $MySQL['banco']);

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

    public function listUsuario($nome,$sobrenome, $telefone, $email) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
            P.NOME,
            P.SOBRENOME,
            P.EMAIL,
            P.TELEFONE 
                from  usuario P
                where 
                    P.NOME like '$nome'
                    and
                    P.SOBRENOME like '$sobrenome' 
                ";

        return $lista = $instanciaConection->listData($query);
    }

    public function saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario) {

        echo $telefone;


        $instanciaConection = self::instanciaConection();

        $query1 = "insert into USUARIO (
                     NOME
                    ,SOBRENOME
                    ,EMAIL
                    ,TELEFONE
                    ,SENHA
                    ) VALUES ( 
                     '$nome' 
                    ,'$sobrenome' 
                    ,'$email'
                    ,'$telefone'
                    ,'$senha_usuario' 
                    );";

        $instanciaConection->executaQuery($query1);
            
    }


}



?>
