package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("file:pedidos?delay=5s&noop=true"). //pasta de origem, o noop é utilizado para que os arquivos não sejam apagados da pasta de origem
				split().			//separa a mensagem por item
					xpath("/pedido/itens/item").
				filter().			//filtra apenas os EBOOKS
					xpath("/item/formato[text()='EBOOK']").
				log("${id}").	//imprime os ids no console
				marshal().xmljson().//transforma arquivo xml em json
				log("${body}").	//imprime o corpo das mensagens no console
				setHeader("CamelFileName", simple("${file:name.noext}.json")). //renomeia os arquivos tirando a extensão original e substituindo por .json
				to("file:saida");	//pasta de destino
				
			}
		});
		
		context.start();
		Thread.sleep(20000);
		context.stop();

	}	
}
