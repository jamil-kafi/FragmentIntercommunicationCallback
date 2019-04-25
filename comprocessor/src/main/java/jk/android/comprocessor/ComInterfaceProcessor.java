package jk.android.comprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import jk.android.intercominterface.ComInterface;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"jk.android.intercominterface.ComInterface"})
@AutoService({Processor.class})
public class ComInterfaceProcessor extends AbstractProcessor {

    // ToDo: Activity, enterAnim, exitAnim, popEnterAnim, popExitAnim, KeyComCallback


    private ProcessingEnvironment processingEnvironment;

    // *****************

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (roundEnvironment.getElementsAnnotatedWith(ComInterface.class).size() > 0) {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(ComInterface.class)) {
                // Element element = roundEnvironment.getElementsAnnotatedWith(ComInterface.class).iterator().next();
                if (element.getKind() == ElementKind.FIELD) {
                    // VariableElement variableElement = (VariableElement) element;
                    createIntercomInterface(element);

                } else {
                    processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "ComInterface annotation is applicable only on Fields!");
                }
            }
        }

        return true;
    }

    // ******************************************

    private void createIntercomInterface(Element processedElement) {

        System.out.print("sout: creating the class...");

        // get the annotation
        ComInterface comInterface = processedElement.getAnnotation(ComInterface.class);

        String parentClassElement = processedElement.getEnclosingElement().getSimpleName().toString();
        String interfaceFinalName = parentClassElement + "InterCom";

        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "messager: creating the class...");

        // ************************************** Generate Fields *********************************

        FieldSpec tagFieldSpec = FieldSpec.builder(String.class, "TAG", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", interfaceFinalName)
                .build();
        FieldSpec keyCallbackFieldSpec = FieldSpec.builder(String.class, "KEY_COM_CALLBACK", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", "com_callback")
                .build();
        FieldSpec activityFieldSpec = FieldSpec.builder(ClassName.get(" androidx.fragment.app", "FragmentActivity"), "activity")
                .addModifiers(Modifier.PRIVATE)
                .build();
        FieldSpec containerFieldSpec = FieldSpec.builder(TypeName.INT, "layoutContainerId", Modifier.PRIVATE)
                // .addAnnotation(ClassName.get("androidx.annotation", "LayoutRes"))
                .initializer("$L", comInterface.containerId())
                .build();
        FieldSpec enterAnimFieldSpec = FieldSpec.builder(TypeName.INT, "enterAnim", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("androidx.annotation", "AnimRes"))
                .initializer("$L", comInterface.enterAnim())
                .build();
        FieldSpec exitAnimFieldSpec = FieldSpec.builder(TypeName.INT, "exitAnim", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("androidx.annotation", "AnimRes"))
                .initializer("$L", comInterface.exitAnim())
                .build();
        FieldSpec popEnterAnimFieldSpec = FieldSpec.builder(TypeName.INT, "popEnterAnim", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("androidx.annotation", "AnimRes"))
                .initializer("$L", comInterface.popEnterAnim())
                .build();
        FieldSpec popExitAnimFieldSpec = FieldSpec.builder(TypeName.INT, "popExitAnim", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("androidx.annotation", "AnimRes"))
                .initializer("$L", comInterface.popExitAnim())
                .build();

        // ************************************** Generate Methods ********************************

        // ************************************** constructor method
        ParameterSpec fragmentActivityParamSpec = ParameterSpec.builder(ClassName.get(" androidx.fragment.app", "FragmentActivity"), "ownerActivity")
                .addAnnotation(ClassName.get("androidx.annotation", "NonNull"))
                .build();
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(fragmentActivityParamSpec)
                .addStatement("$T.out.println($S)", System.class, "Initializing " + interfaceFinalName)
                .beginControlFlow("if (ownerActivity == null)")
                .addStatement("throw new IllegalArgumentException($S)", "Activity parameter cannot be null!")
                .endControlFlow()
                .addStatement("activity = ownerActivity")
                .build();

        // ************************************** popBackStack method
        MethodSpec popBackStackMethod = MethodSpec.methodBuilder("popBackStack")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addException(Exception.class)
                .beginControlFlow("if (activity != null)")
                .addStatement("activity.getSupportFragmentManager().popBackStack()")
                .endControlFlow()
                .build();

        // ************************************** home method
        MethodSpec homeMethod = MethodSpec.methodBuilder("home")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addException(Exception.class)
                .beginControlFlow("if (activity != null)")
                .addStatement("activity.getSupportFragmentManager().popBackStackImmediate(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)")
                .endControlFlow()
                .build();

        // ************************************** switch method
        ParameterSpec fragmentParam = ParameterSpec.builder(ClassName.get("androidx.fragment.app", "Fragment"), "fragment")
                .addAnnotation(ClassName.get("androidx.annotation", "NonNull"))
                .build();
        ParameterSpec addToBackStackParam = ParameterSpec.builder(TypeName.BOOLEAN, "addToBackStack")
                .build();
        ParameterSpec animateParam = ParameterSpec.builder(TypeName.BOOLEAN, "animate")
                .build();
        ParameterSpec replaceParam = ParameterSpec.builder(TypeName.BOOLEAN, "replace")
                .build();
        MethodSpec switchMethod = MethodSpec.methodBuilder("switchFragment")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addException(Exception.class)
                .addParameter(fragmentParam)
                .addParameter(addToBackStackParam)
                .addParameter(animateParam)
                .addParameter(replaceParam)
                .beginControlFlow("if (fragment == null)")
                .addStatement("throw new IllegalArgumentException($S)", "Fragment parameter cannot be null!")
                .endControlFlow()
                .beginControlFlow("if (activity != null)")
                .addStatement("androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager()")
                .addStatement("Fragment visibleFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName())")
                .beginControlFlow("if (visibleFragment != null && visibleFragment.isVisible())")
                .addStatement("return")
                .endControlFlow()
                .addStatement("androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()")
                .beginControlFlow("if (animate && enterAnim != -1 && exitAnim != -1 && popEnterAnim != -1 && popExitAnim != -1)")
                .addStatement("fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)")
                .endControlFlow()
                .beginControlFlow("if (replace)")
                .addStatement("fragmentTransaction.replace(layoutContainerId, fragment, fragment.getClass().getSimpleName())")
                .endControlFlow()
                .beginControlFlow("else")
                .addStatement("fragmentTransaction.add(layoutContainerId, fragment, fragment.getClass().getSimpleName())")
                .endControlFlow()
                .beginControlFlow("if (addToBackStack)")
                .addStatement("fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName())")
                .endControlFlow()
                .addStatement("android.os.Bundle bundle = fragment.getArguments()")
                .beginControlFlow("if (bundle != null)")
                .addStatement("bundle.putSerializable(KEY_COM_CALLBACK, this)")
                .endControlFlow()
                .beginControlFlow("else")
                .addStatement("bundle = new android.os.Bundle(1)")
                .addStatement("bundle.putSerializable(KEY_COM_CALLBACK, this)")
                .endControlFlow()
                .addStatement("fragment.setArguments(bundle)")
                .addStatement("fragmentTransaction.commit()")
                .endControlFlow()
                .build();

        // ************************************** Generate Class **********************************

        TypeSpec intercomClass = TypeSpec.classBuilder(interfaceFinalName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(Serializable.class)
                .addField(activityFieldSpec)
                .addField(tagFieldSpec)
                .addField(keyCallbackFieldSpec)
                .addField(containerFieldSpec)
                .addField(enterAnimFieldSpec)
                .addField(exitAnimFieldSpec)
                .addField(popEnterAnimFieldSpec)
                .addField(popExitAnimFieldSpec)
                .addMethod(constructorMethod)
                .addMethod(popBackStackMethod)
                .addMethod(homeMethod)
                .addMethod(switchMethod)
                .build();

        // ************************************** Generate Class File *****************************
        JavaFile classFile = JavaFile.builder("jk.android.iic", intercomClass)
                .build();
        try {
            classFile.writeTo(processingEnvironment.getFiler());
        } catch (IOException e) {
            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
        }
    }

}
