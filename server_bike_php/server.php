<?php

<<<<<<< HEAD
    $numero_funcao = $_POST['numerofuncao'];
    
=======
	if (isset($_POST['numerofuncao'])) 
	{
		$numero_funcao = $_POST['numerofuncao'];
    } else $numero_funcao = $_GET['numerofuncao'];
>>>>>>> visualizar_dados

    //echo 'Numero funcao: ' .$numero_funcao. '  !!!';

    $userDao = new UsuarioDao();
    $usuario = new Usuario();

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
<<<<<<< HEAD
    case "3":
        $estacao = $_POST['estacao'];   
        $senha_usuario = $_POST['senha'];    

        $usuario->habilitar_vaga($senha_usuario,$estacao);
        break;  
    case "4":
        $estacao = $_POST['estacao'];   
        $senha_usuario = $_POST['senha'];    

        $usuario->retirar_bike($senha_usuario,$estacao);
        break;        
=======
	case "3":
		$email = $_GET['email'];

		$userDao->listUsuario($email);
		break;
>>>>>>> visualizar_dados
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

<<<<<<< HEAD
    public function checkLoginHash( $senha_usuario) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
                1 
                from  USUARIO P
                where 
                    P.SENHA like '$senha_usuario' 
                ";

         $result = $instanciaConection->listData($query);
         
         $contagem = $result->num_rows;  

         //echo $result;      
        if ($contagem > 0) {
            //achou
            return 1;
        } else {
            // nao achou
            return 0;
        }
    }

    public function listUsuario($nome,$sobrenome, $telefone, $email) {
=======
    public function listUsuario($email) {
>>>>>>> visualizar_dados

        $instanciaConection = self::instanciaConection();

        $query = "select 
            P.NOME,
            P.SOBRENOME,
            P.EMAIL,
            P.TELEFONE 
                from  USUARIO P
                where 
<<<<<<< HEAD
                    P.NOME like '$nome'
                    and
                    P.SOBRENOME like '$sobrenome' 
                ;";
=======
                    P.EMAIL like '$email'
                ";
>>>>>>> visualizar_dados

		$lista = $instanciaConection->listData($query);

		$obj = $lista->fetch_object();
		echo "{ 'Nome' : '".$obj->NOME."', 'Sobrenome' : '".$obj->SOBRENOME."', 'Email' : '".$obj->EMAIL."', 'Telefone' : '".$obj->TELEFONE."' } "  ;

        return $obj;
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

<<<<<<< HEAD
    class Usuario
    {

        static private $conection;

        private static function instanciaConection() {

        self::$conection = new ConectionFactory();

        return self::$conection;
    }

        public function habilitar_vaga($senha,$estacao){


            $instanciaConection = self::instanciaConection();

            $userDao = new UsuarioDao();  

            if ($userDao->checkLoginHash($senha) == 1){

                $query_busca = "select ID_ESTACAO from ESTACAO where NOME like '$estacao';"; 
  
                $id_estacao = $instanciaConection->listData($query_busca);

                  if(self::checkVaga() == 1){

                        $query_insert = "insert into USUARIO_ESTACAO
                                (USUARIO_ID_USUARIO,
                                 ESTACAO_ID_ESTACAO,
                                 HORA_INICIO,
                                 HORA_FIM,
                                 CARGA_RESTANTE)
                            select ID_USUARIO,
                                   1,
                                   SYSDATE(),
                                   NULL,
                                   NULL
                            from   USUARIO 
                            where  USUARIO.SENHA like '$senha';";           

                $instanciaConection->executaQuery($query_insert);

                echo "Solicitação de acesso aprovada";
                
                  }
                  else{
                     echo "Essa vaga está ocupada!";
                  }
            }

            else{
                echo "Liberação não autorizada: Senha não reconhecida";
            }
        }

        public function retirar_bike($senha,$estacao){

            $instanciaConection = self::instanciaConection();

            $userDao = new UsuarioDao();  

            if ($userDao->checkLoginHash($senha) == 1){

                $query_busca = "select ID_ESTACAO from ESTACAO where NOME like '$estacao';"; 
  
                $id_estacao = $instanciaConection->listData($query_busca);

                  if(self::checkVaga() == 0){

                        $query_insert = "update USUARIO_ESTACAO set 
                                HORA_FIM = SYSDATE()
                                where HORA_FIM is null
                                and USUARIO_ID_USUARIO = (select ID_USUARIO from USUARIO where SENHA like '$senha');";           

                $instanciaConection->executaQuery($query_insert);

                echo "Vaga liberada para novo uso, pode retirar a bike";
                
                  }
                  else{
                     echo "Nada a fazer";
                  }
            }

            else{
                echo "Liberação não autorizada: Senha não reconhecida";
            }
        }



        public static function checkVaga(){

           $instanciaConection = self::instanciaConection();

           // Se retornar 0 possui vaga
            $query = "select * from USUARIO_ESTACAO where HORA_FIM is null;";

             $result = $instanciaConection->listData($query);
         
             $contagem = $result->num_rows; 

            if ($contagem == 0) {
                return 1;
            } else {
                // Não possui vagas
                return 0;
            }

        }

    }

=======
}
>>>>>>> visualizar_dados
?>
