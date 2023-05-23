# CMPE 172 - Lab #2 Notes

## Spring MVC Project- Serving Web Content with Spring MVC
After running the the spring MVC project on my local machine in port 8080, here is the result:

![Github Webhook](./images/spring-mvc.png)

## Spring Lombok

### ValAndVarUser and FieldLevelGetterSetter DEMO:
![Github Webhook](./images/lombok-val-and-field.png)
![Github Webhook](./images/lombok-field-proof.png)

There is no @setter and @getter annotations for userid, so there are no setter and getter methods in the list. There is @setter and @getter annotations for username, so there are setter and getter methods in the list. There is a @getter annotations and no @setter for userage, so there is only a getter methods in the list.

The val variable has "hello world" while var has 80.

### GetterSetterUserDemo:
![Github Webhook](./images/lombok-getter-setter.png)

There should be setters and getters for all of the field variables, which there are. 

### ConstructorUserDemo:
![Github Webhook](./images/lombok-constructor-test.png)

I added code to show that there are constructors created by the @AllArgsConstructor and @NoArgsConstructor. At the bottom, there is a list of the costructors created.

### DataUserDemo:
![Github Webhook](./images/lombok-datauser.png)

@Data is like having @Getter, @Setter, @ToString, @EqualsAndHashCode and @RequiredArgsConstructor annotations on the class. Here is proof that those exist.

### NonNullUserDemo
![Github Webhook](./images/lombok-nonuser.png)

The username has a nonnull annotation so if null is being passed to this field variables, the output is similar to the image above




