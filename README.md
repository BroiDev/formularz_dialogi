1. Bo CheckBox ma wbudowaną tylko jedną metodę, więc kompilator wie, że chcę ją zaimplementować,
   a SeekBar ma aż trzy, więc, używając lambdy, nie wiedziałby, do której się odwołuję.
2. Np. nasłuchiwacz suwaka SeekBar `OnSeekBarChangeListener` (3 metody) lub nasłuchiwacz listy
   Spinner `OnItemSelectedListener` (2 metody).
3. Przed wyczyszczeniem pól zapisuję stan formularza w klasie pomocniczej `FormState`, tworząc jej
   nową instancję w zmiennej `savedState`. Potem czyszczę formularz, upewniając się, że wszystko
   wyglądać będzie tak samo, jak na początku. Po wyczyszczeniu tworzę SnackBar z opcją cofnięcia,
   do której podpinam metodę cofającą, a jako argument wrzucam wcześniej zapisaną zmienną
   `savedState`. Przywracając dane upewniam się, że np. poprawnie pojawi się pole do wpisania maila,
   jeśli zaznaczony był Switch z newsletterem.