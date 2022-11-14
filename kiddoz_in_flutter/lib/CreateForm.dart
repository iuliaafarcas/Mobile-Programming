import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:kiddoz_in_flutter/utils/Repo.dart';
import 'package:kiddoz_in_flutter/utils/TypeEnum.dart';
import 'package:provider/provider.dart';

const double _kItemExtent = 32.0;

class CreateForm extends StatefulWidget {
  const CreateForm({Key? key}) : super(key: key);

  @override
  State<CreateForm> createState() => _CreateFormState();
}

class _CreateFormState extends State<CreateForm> {
  late TextEditingController _titleController;
  late TextEditingController _descriptionController;
  late TextEditingController _benefitsController;
  late TextEditingController _ageController;

  int selectedType = 0;

  bool _isStringEmpty(String str) {
    if (str == "") return true;
    return false;
  }

  bool _isNumeric(String s) {
    if (s == "") {
      return false;
    }
    return double.tryParse(s) != null;
  }

  void _showDialog(Widget child) {
    showCupertinoModalPopup<void>(
        context: context,
        builder: (BuildContext context) => Container(
              height: 216,
              padding: const EdgeInsets.only(top: 6.0),
              // The Bottom margin is provided to align the popup above the system navigation bar.
              margin: EdgeInsets.only(
                bottom: MediaQuery.of(context).viewInsets.bottom,
              ),
              // Provide a background color for the popup.
              color: CupertinoColors.systemBackground.resolveFrom(context),
              // Use a SafeArea widget to avoid system overlaps.
              child: SafeArea(
                top: false,
                child: child,
              ),
            ));
  }

  @override
  void initState() {
    super.initState();
    _titleController = TextEditingController();
    _descriptionController = TextEditingController();
    _ageController = TextEditingController();
    _benefitsController = TextEditingController();
  }

  @override
  void dispose() {
    _titleController.dispose();
    _descriptionController.dispose();
    _ageController.dispose();
    _benefitsController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final repo = Provider.of<Repo>(context);
    const selectList = TypeEnum.values;
    return CupertinoPageScaffold(
        child: Padding(
      padding: const EdgeInsetsDirectional.only(start: 15, end: 15),
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        const SizedBox(
          height: 10,
        ),
        const Text("Title"),
        const SizedBox(
          height: 5,
        ),
        CupertinoTextField(
          controller: _titleController,
          placeholder: "Title...",
        ),
        const SizedBox(
          height: 10,
        ),
        const Text("Description"),
        const SizedBox(
          height: 5,
        ),
        CupertinoTextField(
          controller: _descriptionController,
          placeholder: "Description...",
        ),
        const SizedBox(
          height: 10,
        ),
        const Text("Benefits"),
        const SizedBox(
          height: 5,
        ),
        CupertinoTextField(
          controller: _benefitsController,
          placeholder: "Benefits...",
        ),
        const SizedBox(
          height: 10,
        ),
        const Text("Recommended age"),
        const SizedBox(
          height: 5,
        ),
        CupertinoTextField(
          controller: _ageController,
          placeholder: "Recommended age...",
        ),
        const SizedBox(
          height: 10,
        ),
        const Text("Type"),
        const SizedBox(
          height: 5,
        ),
        CupertinoButton.filled(
          padding: EdgeInsets.zero,
          // Display a CupertinoPicker with list of fruits.
          onPressed: () => _showDialog(
            CupertinoPicker(
              magnification: 1.22,
              squeeze: 1.2,
              useMagnifier: true,
              itemExtent: _kItemExtent,
              // This is called when selected item is changed.
              onSelectedItemChanged: (int selectedItem) {
                setState(() {
                  selectedType = selectedItem;
                });
              },
              children: List<Widget>.generate(selectList.length, (int index) {
                return Center(
                  child: Text(
                    EnumHelper.getValue(selectList[index]),
                  ),
                );
              }),
            ),
          ),
          // This displays the selected fruit name.
          child: Text(
            EnumHelper.getValue(selectList[selectedType]),
            style: const TextStyle(
              fontSize: 16.0,
            ),
          ),
        ),
        const SizedBox(
          height: 30,
        ),
        CupertinoButton.filled(
          //padding: const EdgeInsetsDirectional.only(start: 15, end: 15),
          onPressed: () {
            if (_isStringEmpty(_titleController.text) ||
                _isStringEmpty(_descriptionController.text) ||
                _isStringEmpty(_benefitsController.text) ||
                !_isNumeric(_ageController.text)) {
              ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                content: Text("Invalid fields!"),
              ));
            } else {
              repo.addToList(
                  _titleController.text,
                  _descriptionController.text,
                  _benefitsController.text,
                  int.parse(_ageController.text),
                  selectList[selectedType]);
              // Navigator.of(context).pop();
            }
          },
          child: const Text('Add'),
        ),
      ]),
    ));
  }
}
