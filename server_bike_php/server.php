<?php


    $numero_funcao = $_POST['numerofuncao'];
    

    //echo 'Numero funcao: ' .$numero_funcao. '  !!!';

    $userDao = new UsuarioDao();

    switch ($numero_funcao) {
    case "1":
        $nome = $_POST['nome'];
        $sobrenome = $_POST['sobrenome'];
        $email = $_POST['email'];
        $telefone = $_POST['telefone'];
        $senha_usuario = $_POST['senha'];

        $userDao->saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario);
        break;
    case "2":
        $email = $_POST['email'];
        $senha_usuario = $_POST['senha'];    

        $userDao->checkLogin($email, $senha_usuario);
        break;
}

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



    public function checkLogin($email, $senha_usuario) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
                1 
                from  USUARIO P
                where 
                    P.EMAIL like '$email'
                    and
                    P.SENHA like '$senha_usuario' 
                ";

         $result = $instanciaConection->listData($query);
         
         $contagem = $result->num_rows;  

         //echo $result;      

        if ($contagem > 0) {
            echo "loguei";
            return 1;
        } else {
            echo "naologuei";
            return 0;
        }
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

        echo "Dados recebidos e salvos no servidor";


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
