(ns threejs-book.ch6a)

;; The vertical extent of the canvas in world space (this is basically defining
;; your visible portion of the scene in vertical units - horizontal extents
;; depend on window's aspect ratio).
(def ymax 2)

(def THREE js/THREE)
(def canvas (.getElementById js/document "main_canvas"))

(defonce context (atom {}))

(defn init-gui []
  (let [gui (:gui @context)
        controls (:controls @context)]
    (.add gui controls "rotationSpeedX" 0 0.1)
    (.add gui controls "rotationSpeedY" 0 0.1)))

(defn set-camera-params []
  (let [camera (:camera @context)
        ratio (/ (.-clientWidth canvas) (.-clientHeight canvas))
        xmax (* ymax ratio)
        xmax2 (/ xmax 2)
        ymax2 (/ ymax 2)]
    (set! (.-left camera) (- xmax2))
    (set! (.-right camera) xmax2)
    (set! (.-top camera) ymax2)
    (set! (.-bottom camera) (- ymax2))
    (set! (.-near camera) 0.1)
    (set! (.-far camera) 1000)
    (set! (.-z (.-position camera)) 3)
    (.updateProjectionMatrix camera)))

(defn on-resize []
  (let [renderer (:renderer @context)
        width (.-innerWidth js/window)
        height (.-innerHeight js/window)]
    (set! (.-width canvas) width)
    (set! (.-height canvas) height)
    (.setViewport renderer 0 0 width height)
    (set-camera-params)))

(defn random-vector []
  (let [rand-coord (fn [] (- (* 2 (.random js/Math)) 1))]
    (THREE.Vector3. (rand-coord) (rand-coord) (rand-coord))))

(defn generate-mesh []
  (let [points-per-mesh 5
        geometry (THREE.ConvexGeometry. (to-array (repeatedly points-per-mesh
                                                              random-vector)))
        material (THREE.MeshBasicMaterial. #js {:wireframe true :color 0x288CFF})]
    (THREE.Mesh. geometry material)))

(defn generate-meshes []
  (let [mesh-count 10]
    (repeatedly mesh-count generate-mesh)))

(defn on-click []
  (let [scene (:scene @context)
        meshes (generate-meshes)]
    (doseq [mesh (:meshes @context)] (.remove scene mesh))
    (reset! context (conj @context {:meshes meshes}))
    (doseq [mesh meshes] (.add scene mesh))))

(defn rotate-meshes []
  (let [meshes (:meshes @context)
        controls (:controls @context)]
    (doseq [mesh meshes]
      (set! (.-x (.-rotation mesh))
            (+ (.-x (.-rotation mesh)) (.-rotationSpeedX controls)))
      (set! (.-y (.-rotation mesh)) (+ (.-y (.-rotation mesh))
                                       (.-rotationSpeedY controls))))))

(defn animate []
  (let [request-id (.requestAnimationFrame js/window animate)]
    (reset! context (conj @context {:request-id request-id}))
    
    ;; Do your "update stuff" here
    (rotate-meshes)
    
    ;; Render everything
    (.render (:renderer @context) (:scene @context) (:camera @context))))

;; Figwheel live reloading hack - the start fn should be invoked only once
(defonce started (atom false))

(defn start []
  (let [renderer (THREE.WebGLRenderer. #js {:canvas canvas})
        scene (THREE.Scene.)
        camera (THREE.OrthographicCamera. 0 0 0 0 0 0)
        geometry (THREE.BoxGeometry. 1 1 1)
        material (THREE.LineBasicMaterial. #js {:color 0x00FF00})
        meshes (generate-meshes)
        light (THREE.DirectionalLight. 0xffffff 1.0)
        dat-controls #js {:rotationSpeedX 0.007
                          :rotationSpeedY 0.002}
        dat-gui (js/dat.GUI.)
        width (.-innerWidth js/window)
        height (.-innerHeight js/window)]
    
    (reset! context {:renderer renderer
                     :scene scene
                     :camera camera
                     :meshes meshes
                     :gui dat-gui
                     :controls dat-controls})
    
    (set! (.-width canvas) width)
    (set! (.-height canvas) height)
    (.setViewport renderer 0 0 width height)
    
    (set-camera-params)
    
    (doseq [mesh meshes] (.add scene mesh))
    
    (.set (.-position light) 0 0 1)
    (.add scene light)
    
    (.addEventListener js/window "resize" on-resize)
    (.addEventListener js/window "mousedown" on-click)
    
    (init-gui)))

(defn main []
  (let [request-id (:request-id @context)]
    ;; Init
    (if (not @started) (do (start) (reset! started true)))
    
    ;; Kill the old animate function, if it exists
    (if request-id (.cancelAnimationFrame js/window request-id))
    
    ;; Register a new animate handler
    (.requestAnimationFrame js/window animate)))
