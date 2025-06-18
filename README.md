# Bachelor_Project - TreeHelper - Wood Transport Authorization and Monitoring System


Forests are an indispensable element of a natural world, one which we are in contact with, no matter how we live our lives. From their impact in healthy ecosystems, biodiversity and benefits to cleaner air, to the materials for our furniture, paper or buildings, forests have an impact everywhere, and their importance should be uncontested.

Humanity has had to deal with the problem of deforestations for a long time, but because of the technological advancements of the last century, new ways of doing such activities have arisen and have led to a large increase in wood theft. National authorities all around the globe have implemented various laws and mechanisms to put a stop to illegal logging activities, and results can be seen, but the problem persists. We, as a society, need to continue to do whatever is in our power to minimize illegal deforestation.

Digitalization plays an important part, with governments implementing IT solutions that are efficient. In Romania, efforts have been made, a great example being the widely used SUMAL system, but a general solution which encapsulates software, hardware, communications and artificial intelligence has not been implemented at a large scale yet.

This paper proposes TreeHelper, an IoT solution that aims to improve authorization and monitoring practices, in order to help authorities act faster and save important elements of the environment. It is composed of two important parts: a web platform and an edge AI device to be placed on the routes of tree logging trucks. The web platform is built using Spring Boot for the backend, React for the frontend and PostgreSQL as the database. It allows transporters to request wood transport authorizations in a straight-forward manner, while giving authorities the chance to review and decide upon these requests. The smart monitoring device consists of a Raspberry Pi for processing, a camera for capturing live video, a Coral USB Accelerator in order to accelerate model inference and a SIM7600 4G HAT for communication and GPS data acquisition. The used model is YOLOv11n and it is trained on a custom dataset of tree logging truck images.

Model inference is run on the frames of the live camera feed and if a truck is detected, the frame is sent to a cloud ALPR service in order to extract the license plate number. Then, using the 4G connection, the license plate number is sent to the backend, and a check for an associated authorization is done. If nothing is found, the authorities are alerted through an SMS message containing the license plate number and the GPS coordinates, so they can act accordingly.




