import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.lang.Math;
import org.postgresql.*;

class AtualizaBase{  
	public static void main(String[] args){
		Date dtDataHoraIni = new Date( );
		System.out.println("Data/Hora Início: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format( dtDataHoraIni ) );
		
		if( args == null || args.length == 0 ) {
			System.out.println( "Arquivo não informado!");
			return;
		}

		BufferedReader buffer = openBufferedReader( args[0] );
		processFile( buffer );
		closeBufferedReader( buffer );
		
		Date dtDataHoraFim = new Date( );
		System.out.println("Data/Hora Fim: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format( dtDataHoraFim ) );
	}

	private static BufferedReader openBufferedReader( String strFile ) {
		BufferedReader buffer = null;

		try {
			File file = new File( strFile );
			buffer = new BufferedReader( new InputStreamReader( new FileInputStream(file), "UTF8"));
		} catch( Exception e ) {
			System.out.println( "Não foi possível abrir o arquivo." );
			System.out.println(e);
		}

		return buffer;
	}

	private static void closeBufferedReader( BufferedReader buffer ) {
		try {
			if( buffer != null )
				buffer.close();

		} catch( Exception e ) {
			System.out.println( "Não foi possível fechar o arquivo." );
			System.out.println(e);			
		}
	}

	private static void processFile( BufferedReader buffer ) {
		if( buffer == null )
			return;

		long lLinha = 0;
		int nTotRegAtu = 0;

		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");  
			con = DriverManager.getConnection( "jdbc:postgresql://<url>/<database>","<user>","<password>");
			stmt = con.createStatement();

			String strLinha = "";
			String[] astrValores;

			long key1 = 0;
			int  key2 = 0;
			long key3 = 0;

			boolean bPrimeiraVez = true;

			while( (strLinha = buffer.readLine()) != null ){
				if( bPrimeiraVez ) {
					bPrimeiraVez = false;
					continue;
				}

				lLinha++;

				astrValores = strLinha.split(";");
				if( astrValores == null || astrValores.length == 0 )
					continue;

				key1 = Long.parseLong( astrValores[0] );
				key2 = Integer.parseInt( astrValores[1] );
				key3 = Long.parseLong( astrValores[2] );

				nTotRegAtu += stmt.executeUpdate( "UPDATE <table> SET <field> = <value> WHERE <key1> = " + matricula_id + " AND <key2> = " + key2 + " AND <key3> = " + key3 + ";" );
			}
		} catch(Exception e) {
			System.out.println( "Linha: " + lLinha );
			System.out.println( "Ocorreu um erro durante o processamento do arquivo." );	
			System.out.println(e);
		} finally {
			try {
				if( con != null )
					con.close();
				
				if( stmt != null )
					stmt.close();				
			} catch( Exception e ) {
				System.out.println( "Ocorreu um erro ao tentar fechar as conexões." );
				System.out.println( e );
			}
		}

		System.out.println( "Total de registros processados: " + nTotRegAtu );
	}
}