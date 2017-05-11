# PSE Móvil inside

PSE Móvil permite a los clientes que utilizan dispositivos móviles autorizar pagos desde una APP en vez de usar el navegador Web. Esto permite mayor control, recordar credenciales de forma segura y una experiencia nativa.

PSE Móvil puede ser embebido directamente en las aplicaciones de los comercios y con esto mejorar notablemente la experiencia del usuario al mantener todo el proceso de compra dentro de la APP del comerico.

Este repositorio es ejemplo y documentación del proceso para integrar la tecnología Browser2App (usada en PSE Móvil) en la APP del comercio.


# Manual de uso la biblioteca nativa Browser2app en Android (com.browser2app:khenshin:*) 

Esta aplicación ha sido creada para demostrar la utilización de nuestra biblioteca khenshin.
Para poder ejecutar esta aplicación es necesario que tengas acceso a nuestro repositorio privado: https://dev.khipu.com/nexus/content/repositories/browser2app

Los pasos necesarios para utilizar la biblioteca nativa android para Browser2app son:

1. [Agregar los repositorios](#repositorios)
2. [Agregar las dependencias](#dependencias)
3. [Modificar la clase base de tu app](#clase-de-tu-aplicación)
4. [Configurar colores](#colores) y [vistas del proceso](#vistas)
5. [Invocar browser2app desde tu app](#invocación)
6. [Recibir la respuesta en tu app](#respuesta)


## Repositorios

Se debe incluir el [repositorio maven de khenshin](https://dev.khipu.com/nexus/content/repositories/browser2app) así como jcenter y el repositorio del proyecto [dsl4xml](https://github.com/steveliles/dsl4xml)


    allprojects {
		repositories {
			jcenter()
			maven {
				url 'http://steveliles.github.com/repository/'
			}
			maven {
				url 'https://dev.khipu.com/nexus/content/repositories/browser2app'
				credentials {
					username khenshinRepoUsername
					password khenshinRepoPassword
				}
			}
		}
	}
	
Los campos khenshinRepoUsername y khenshinRepoPassword te serán proporcionados por tu ejecutivo de ACH Colombia, se deben incluir en el archivo gradle.properties en la raiz del proyecto y sin incluir al sistema de control de versiones.

## Dependencias

Con los repositorios agregados puedes agregar el paquete khenshin a tu proyecto.

    compile 'com.browser2app:khenshin:2.0.7'
    
## Clase de tu aplicación

La clase principal de tu aplicación (la definida en el atributo android:name dentro del tag application en el AndroidManifest.xml) debe implementar la interfaz KhenshinApplication y en el constructor debe inicializar a Khenshin

	public class PSEInsideDemo extends Application implements KhenshinApplication {
	
		...
		
		private KhenshinInterface khenshin;
	
		@Override
		public KhenshinInterface getKhenshin() {
			return khenshin;
		}
	
		...
	
		public PSEInsideDemo() {
			super();
			khenshin = new Khenshin.KhenshinBuilder()
					.setApplication(this)
					.setAutomatonAPIUrl("https://b2a.pse.com.co/api/automata/")
					.setCerebroAPIUrl("https://b2a.pse.com.co/api/automata/")
					.setMainButtonStyle(Khenshin.CONTINUE_BUTTON_IN_FORM)
					.setAllowCredentialsSaving(true)
					.setHideWebAddressInformationInForm(true)
					.build();
		}
		
		...
	
	}
	


El parámetro MainButtonStyle puede tomar los valores Khenshin.CONTINUE_BUTTON_IN_FORM (El botón principal se pinta en bajo el formulario) o Khenshin.CONTINUE_BUTTON_IN_TOOLBAR (El botón principal se pinta en la barra de navegación).

Con AllowCredentialsSaving se enciende o apaga la opción de recordar credenciales en el dispositivo.

Con HideWebAddressInformationInForm se esconde la URL de navegación.


## Colores

En tu proyecto puedes determinar los colores que usará Khenshin en las pantallas de pago sobreescribiendo los siguiente parámetros en los recursos de tu proyecto (por ejemplo en un archivo colors.xml dentro de res/values)

    <color name="khenshin_primary">#ca0814</color> <!-- Color de la barra de navegación y botón principal-->
    <color name="khenshin_primary_dark">#580409</color> <!-- Color del status bar superior -->
    <color name="khenshin_primary_text">#ffffff</color> <!-- Color del texto en la barra de navegación -->
    <color name="khenshin_accent">#ca0814</color> <!-- Color de las decoraciones, por ejemplo barras de progreso -->
    
## Vistas

Para personalizar más aún la visualización de Khenshin puedes sobreescribir archivos de layout que se utilizan en el proceso de pago:


### khenshin_toolbar_title.xml

Este layout se usa en la barra de navegación en las páginas de salida (exito, fracaso o advertencia) y en las páginas del proceso si la barra del navegador está oculta.

	<?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        style="@style/khenshin_toolbar_title">
        <TextView
            style="@style/khenshin_toolbar_title_text"
            android:text="@string/app_name"
        />
    </LinearLayout>

### khenshin_process_header.xml

Este layout se utiliza en todas las páginas intermedias del proceso de pago. Se incluye la implementación usada en PSE Móvil:

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
				  style="@style/khenshin_vertical_wrapper">
		<ImageView
				style="@style/pament_header_image"
				android:id="@+id/merchantImage"/>
		<TextView
				android:id="@+id/merchantName"
				style="@style/khenshin_pay_title"
				android:layout_marginTop="10dp"
				android:layout_marginLeft="10dp"
				android:layout_marginRight="10dp"/>
	
		<LinearLayout
				android:layout_width="fill_parent"
				android:layout_height="wrap_content"
				android:layout_marginLeft="10dp"
				android:layout_marginRight="10dp"
				android:orientation="horizontal">
			<TextView
					style="@style/khenshin_dialog_title"
					android:layout_width="0dp"
					android:layout_weight=".8"
					android:layout_height="wrap_content"
					android:id="@+id/subject"/>
			<TextView
					style="@style/khenshin_dialog_title"
					android:layout_width="0dp"
					android:layout_weight=".2"
					android:gravity="right"
					android:layout_height="wrap_content"
					android:id="@+id/amount"/>
		</LinearLayout>
	
	</LinearLayout>

    
    
Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- ImageView: android:id="@+id/merchantImage"
- TextView: android:id="@+id/merchantName"
- TextView: android:id="@+id/subject"
- TextView: android:id="@+id/amount"
- TextView: android:id="@+id/paymentMethod"
    

### khenshin_process_success.xml

Se utiliza al finalizar el proceso de manera exitosa. La implementación incluida es:

	<?xml version="1.0" encoding="utf-8"?>
	<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
				style="@style/khenshin_page_body">
	
	
		<LinearLayout style="@style/khenshin_vertical_wrapper">
	
			<include layout="@layout/khenshin_finish_header"/>
			<LinearLayout style="@style/khenshin_vertical_wrapper_padded">
				<View style="@style/khenshin_horizontal_separator"/>
	
				<TextView
						android:id="@+id/title"
						style="@style/khenshin_dialog_title"/>
	
				<ImageView
						android:id="@+id/exitImage"
						android:layout_height="wrap_content"
						android:layout_width="wrap_content"
						android:layout_gravity="center_horizontal"
						android:src="@drawable/ic_transfer_ok"
						android:paddingBottom="10dp"/>
	
				<TextView
						android:id="@+id/message"
						style="@style/khenshin_dialog_message"
				/>
	
				<Button android:id="@+id/nextButton" android:visibility="gone" android:layout_width="match_parent" android:layout_height="wrap_content"
						android:text="@string/khenshinFinish" style="@style/khenshin_button"/>
			</LinearLayout>
	
		</LinearLayout>
	
	
	</ScrollView>
    
Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- TextView: android:id="@+id/title"
- TextView: android:id="@+id/message"

### khenshin_process_warning.xml

	<?xml version="1.0" encoding="utf-8"?>
	<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
				style="@style/khenshin_page_body">
	
	
		<LinearLayout style="@style/khenshin_vertical_wrapper">
	
			<include layout="@layout/khenshin_finish_header"/>
	
			<LinearLayout style="@style/khenshin_vertical_wrapper_padded">
	
				<View style="@style/khenshin_horizontal_separator"/>
	
				<TextView
						android:id="@+id/title"
						style="@style/khenshin_dialog_title"/>
	
	
				<ImageView
						android:id="@+id/exitImage"
						android:layout_height="wrap_content"
						android:layout_width="wrap_content"
						android:layout_gravity="center_horizontal"
						android:src="@drawable/ic_transfer_warning"
						android:paddingBottom="10dp"/>
	
				<TextView
						android:id="@+id/message"
						style="@style/khenshin_dialog_message"
				/>
	
				<Button android:id="@+id/nextButton" android:visibility="gone" android:layout_width="match_parent" android:layout_height="wrap_content"
						android:text="@string/khenshinFinish" style="@style/khenshin_button"/>
	
			</LinearLayout>
		</LinearLayout>
	
	
	</ScrollView>

Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- TextView: android:id="@+id/title"
- TextView: android:id="@+id/message"

### khenshin_process_failure.xml

	<?xml version="1.0" encoding="utf-8"?>
	<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
				style="@style/khenshin_page_body">
	
	
		<LinearLayout style="@style/khenshin_vertical_wrapper_padded">
	
			<TextView
				android:id="@+id/title"
				style="@style/khenshin_dialog_title"/>
	
	
			<ImageView
				android:id="@+id/exitImage"
				android:layout_height="wrap_content"
				android:layout_width="wrap_content"
				android:layout_gravity="center_horizontal"
				android:src="@drawable/ic_transfer_error"
				android:paddingBottom="10dp"/>
	
			<TextView
				android:id="@+id/message"
				style="@style/khenshin_dialog_message"
				/>
	
			<Button android:id="@+id/nextButton" android:visibility="gone" android:layout_width="match_parent" android:layout_height="wrap_content"
					android:text="@string/khenshinFinish" style="@style/khenshin_button"/>
	
	
		</LinearLayout>
	
	
	</ScrollView>
    
Khenshin reemplazará los valores de los siguientes campos (Tipo y android:id)

- TextView: android:id="@+id/title"
- TextView: android:id="@+id/message"    
    
## Invocación

Antes de invocar la biblioteca es necesario utilizar la API de ACH Colombia para generar un pago de manera estandar hasta el punto en que se obtiene la URL de redirección hacia el registro PSE que es de la forma

	https://registro.pse.com.co/PSEUserRegister/StartTransaction.htm?enc=XXXXXXXXXX


Al valor del parámetro "enc" de la url anterior le llamaremos ecus o cus encodeado.
 
Adicionalmente a eso se debe saber el identificador de la entidad financiera autorizadora y el tipo de cliente que ha sido seleccionado según los mismos códigos usados al momento de consultarle al usuario su banco, es decir:

Entidades financieras autorizadoras:
- 1040: BANCO AGRARIO
- 1052: BANCO AV VILLAS
- 1013: BANCO BBVA COLOMBIA S.A.
- 1032: BANCO CAJA SOCIAL
- 1019: BANCO COLPATRIA
- 1066: BANCO COOPERATIVO COOPCENTRAL
- 1006: BANCO CORPBANCA S.A
- 1051: BANCO DAVIVIENDA
- 1001: BANCO DE BOGOTA
- 1023: BANCO DE OCCIDENTE
- 1062: BANCO FALABELLA 
- 1012: BANCO GNB SUDAMERIS
- 1060: BANCO PICHINCHA S.A.
- 1002: BANCO POPULAR
- 1058: BANCO PROCREDIT
- 1007: BANCOLOMBIA
- 1061: BANCOOMEVA S.A.
- 1009: CITIBANK 
- 1014: HELM BANK S.A.
- 1507: NEQUI

Tipos de usuario:
- 0: Persona natural
- 1: Persona jurídica

Con estos datos se puede realizar la invocación:

	String automatonId = userTypeId + authorizerId;

	intent.putExtra(KhenshinConstants.EXTRA_AUTOMATON_ID, automatonId);
	Bundle params = new Bundle();

	params.putString("cus", ecus);
	params.putString("amount", amount); // formato decimal con dos cífras significativas, ej 10.00
	params.putString("authorizerId", authorizerId); // id de entidad autorizadora
	params.putString("subject", subject); // motivo del pago tal y como fue creado usando los webservices de ACH
	params.putString("merchant", merchant); // nombre del comercio registrado en ACH
	params.putString("cancelURL", returnURL); // url de retorno al comercio
	params.putString("paymentId", ecus);
	params.putString("userType", userTypeId); // id del tipo de usuario
	params.putString("returnURL", returnURL); // url de retorno al comercio
	params.putString("payerEmail", payerEmail); // correo electrónico del pagador

	intent.putExtra(KhenshinConstants.EXTRA_AUTOMATON_PARAMETERS, params);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivityForResult(intent, START_PAYMENT_REQUEST_CODE);

Todos los campos deben coincidir exáctamente con los utilizados al momento de crear el pago usando los webservices de ACH si algún campo no coincide la aplicación le mostrará un mensaje de error del tipo "Datos inconsistentes".

Lo mismo ocurre con el campo "merchant" que debe coincidir exáctamente con el nombre del comercio registrado en ACH Colombia, por ejemplo si el nombre del comercio es "ACH Colombia S.A." e intentamos iniciar un pago entregando en el parámetro "merchant" la cadena "ACH Colombia" el pago fallará con error de "Datos inconsistentes".

## Respuesta

En la actividad de tu aplicación que inició la actividad de pago se debe implementar el método onActivityResult

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == START_PAYMENT_REQUEST_CODE) {
			String exitUrl = data.getStringExtra(KhenshinConstants.EXTRA_INTENT_URL);
			if (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, "PAYMENT OK, exit url: " + exitUrl,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MainActivity.this, "PAYMENT FAILED, exit url: " + exitUrl,
						Toast.LENGTH_LONG).show();
			}
		}
	
	}
	
El parámetro requestCode debe ser el mismo que se envió al iniciar la actividad.

El parámetro resultCode será RESULT_OK si el pago terminó exitósamente o RESULT_CANCEL si el usuario no completó el pago.

En data vendrá un extra de nombre KhenshinConstants.EXTRA_INTENT_URL que tendrá la URL de salida definida al momento de crear el pago.

    